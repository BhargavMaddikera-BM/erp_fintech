package com.blackstrawai.payroll;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ApplicationRuntimeException;
import com.blackstrawai.DateConverter;
import com.blackstrawai.ap.dropdowns.BasicCurrencyVo;
import com.blackstrawai.ap.dropdowns.PaymentTypeVo;
import com.blackstrawai.ap.payment.noncore.PayrollDueItemVo;
import com.blackstrawai.ap.payment.noncore.PayrollDueVo;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.attachments.AttachmentsDao;
import com.blackstrawai.attachments.AttachmentsVo;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.journals.GeneralLedgerVo;
import com.blackstrawai.payroll.payrun.PayRunEmployeeAmountVo;
import com.blackstrawai.payroll.payrun.PayRunEmployeePaymentVo;
import com.blackstrawai.payroll.payrun.PayRunEmployeeVo;
import com.blackstrawai.payroll.payrun.PayRunPaymentCycleVo;
import com.blackstrawai.payroll.payrun.PayRunPaymentTypeVo;
import com.blackstrawai.payroll.payrun.PayRunPeriodVo;
import com.blackstrawai.payroll.payrun.PayRunTableVo;
import com.blackstrawai.payroll.payrun.PayRunVo;
import com.blackstrawai.settings.CurrencyDao;
import com.blackstrawai.settings.CurrencyVo;
import com.blackstrawai.upload.PayrollUploadVo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.Statement;

@Repository
public class PayRunDao extends BaseDao {
	private Logger logger = Logger.getLogger(PayRunDao.class);

	@Autowired
	private AttachmentsDao attachmentsDao;

	@Autowired
	private CurrencyDao currencyDao;

	public PayRunVo createPayRun(PayRunVo payRunVo) throws ApplicationException {
		logger.info("To create a new Pay run" + payRunVo);
		Connection connection = null;
		if (payRunVo.getOrgId() != null && payRunVo.getUserId() != null) {
			try {
				connection = getPayrollConnection();
				connection.setAutoCommit(false);
				if(payRunVo.getOrgId()!=null) {
				BasicCurrencyVo defaultCurrency = currencyDao.getDefaultCurrencyForOrganization(payRunVo.getOrgId(), connection);
				payRunVo.setCurrencyId(defaultCurrency!=null&&defaultCurrency.getId()>0?defaultCurrency.getId():0);
				}
				createPayRunData(payRunVo, connection);
				if (payRunVo.getAttachments() != null) {
					attachmentsDao.createAttachments(payRunVo.getOrgId(), payRunVo.getUserId(),
							payRunVo.getAttachments(), AttachmentsConstants.MODULE_TYPE_PAYROLL_PAYRUN,
							payRunVo.getPayRunId(), payRunVo.getRoleName());
				}

				connection.commit();
				logger.info("Succecssfully created Pay run in PayRunDao" + payRunVo);
			} catch (Exception e) {
				logger.info("Error in Pay Run creation :: ", e);
				try {
					connection.rollback();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					throw new ApplicationException(e1.getMessage());
				}
				logger.info("Before throw in dao :: " + e.getMessage());
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(null, null, connection);
			}
		} else {
			throw new ApplicationException("No Data for PayRun Organization and User Id");
		}
		return payRunVo;
	}


	private PayRunVo createPayRunData(PayRunVo payRunVo, Connection connection) throws ApplicationException {

		PreparedStatement preparedStatementrun = null;
		String payRunReference = null;
		if (!payRunVo.getPayRunRefPrefix().isEmpty() && !payRunVo.getPayRunRefSuffix().isEmpty()) {
			StringBuilder payRunReferenceBuilder = new StringBuilder();
			payRunReferenceBuilder
					.append(payRunVo.getPayRunRefPrefix() != null && !payRunVo.getPayRunRefPrefix().isEmpty()
							? payRunVo.getPayRunRefPrefix()
							: " ")
					.append("~").append(payRunVo.getPayrunReference()).append("~")
					.append(payRunVo.getPayRunRefSuffix() != null && !payRunVo.getPayRunRefSuffix().isEmpty()
							? payRunVo.getPayRunRefSuffix()
							: " ");
			payRunReference = payRunReferenceBuilder.toString();
		}
		if (payRunVo.getPayRunRefPrefix() != null && payRunVo.getPayRunRefSuffix().isEmpty()) {
			StringBuilder payRunReferenceBuilder = new StringBuilder();
			payRunReferenceBuilder
					.append(payRunVo.getPayRunRefPrefix() != null && !payRunVo.getPayRunRefPrefix().isEmpty()
							? payRunVo.getPayRunRefPrefix()
							: " ")
					.append("~").append(payRunVo.getPayrunReference());
			payRunReference = payRunReferenceBuilder.toString();
		}
		if (payRunVo.getPayRunRefPrefix().isEmpty() && payRunVo.getPayRunRefSuffix() != null) {
			StringBuilder payRunReferenceBuilder = new StringBuilder();
			payRunReferenceBuilder.append(payRunVo.getPayrunReference()).append("~")
					.append(payRunVo.getPayRunRefSuffix() != null && !payRunVo.getPayRunRefSuffix().isEmpty()
							? payRunVo.getPayRunRefSuffix()
							: " ");
			payRunReference = payRunReferenceBuilder.toString();
		}

		if (payRunVo.getPayRunRefPrefix().isEmpty() && payRunVo.getPayRunRefSuffix().isEmpty()) {
			payRunReference = payRunVo.getPayrunReference();
		}

		boolean isPayRunReferenceExist = checkPayRunReferenceExistForAnOrganization(payRunVo.getOrgId(), connection,
				payRunReference);
		if (isPayRunReferenceExist) {
			throw new ApplicationException("PayRun Exist for the Organization");
		}

		try {
			preparedStatementrun = connection.prepareStatement(PayRunConstants.INSERT_INTO_PAY_RUN,
					Statement.RETURN_GENERATED_KEYS);
			preparedStatementrun.setString(1, payRunReference != null ? payRunReference : null);
			preparedStatementrun.setString(2, payRunVo.getPayrunDate() != null ? DateConverter.getInstance().correctDatePickerDateToString(payRunVo.getPayrunDate())	: null);
			preparedStatementrun.setString(3, payRunVo.getPayPeriod() != null ? payRunVo.getPayPeriod() : null);
			preparedStatementrun.setString(4, payRunVo.getStatus() != null ? payRunVo.getStatus() : null);
			preparedStatementrun.setString(5, payRunVo.getUserId() != null ? payRunVo.getUserId() : null);
			preparedStatementrun.setLong(6, payRunVo.getOrgId() != null ? payRunVo.getOrgId() : 0);
			preparedStatementrun.setString(7, payRunVo.getRoleName() != null ? payRunVo.getRoleName() : null);
			preparedStatementrun.setString(8,
					payRunVo.getCopyPreviousPayRun() != null ? payRunVo.getCopyPreviousPayRun() : null);
			preparedStatementrun.setString(9, payRunVo.getPaymentCycle() != null ? payRunVo.getPaymentCycle() : null);

			ObjectMapper mapper = new ObjectMapper();
			String glData = payRunVo.getGeneralLedgerData() != null
					? mapper.writeValueAsString(payRunVo.getGeneralLedgerData())
					: null;
			logger.info("glData>> " + glData);
			preparedStatementrun.setString(10, glData);
			String jsn = mapper.writeValueAsString(payRunVo.getPayRunInformation());
			logger.info("Insert info " + jsn);
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> payrunMapList = mapper.readValue(jsn, ArrayList.class);
			String employeeId = null;
			Double earnings = 0.0;
			Double deductions = 0.0;

			Double payRunAmount = 0.0;
			List<String> employeeList = new ArrayList<>();

			List<PayRunEmployeePaymentVo> employeePayRunList = new LinkedList<PayRunEmployeePaymentVo>();

			for (Map<String, Object> map : payrunMapList) {
				earnings = 0.0;
				deductions = 0.0;
				PayRunEmployeePaymentVo payRunEmployeePaymentVo=new PayRunEmployeePaymentVo();
				for (Map.Entry<String, Object> entry : map.entrySet()) {
										payRunEmployeePaymentVo.setPayrunReference(payRunReference);
					if (entry.getKey().contains("employeeId")) {
						employeeId = String.valueOf(entry.getValue());
						employeeList.add(employeeId);
						logger.info("pay run object employee--- " + employeeId);
						payRunEmployeePaymentVo.setEmpId(employeeId);
					}

					if (employeeList.contains(employeeId)) {
						if (entry.getKey().contains("earnings")) {
							logger.info("earnings--" + entry.getKey() + "::" + entry.getValue());
							earnings += Double.parseDouble(entry.getValue().toString());
						} else if (entry.getKey().contains("deductions")) {
							logger.info("deductions--" + entry.getKey() + "::" + entry.getValue());
							deductions += Double.parseDouble(entry.getValue().toString());
						}
						if (entry.getKey().contains("totalEarnings")) {

							entry.setValue(earnings);
							logger.info("Total - earnings " + entry.getValue());
						}
						if (entry.getKey().contains("totalDeductions")) {

							entry.setValue(deductions);
							logger.info("Total - Deductions " + entry.getValue());
						}
						if (entry.getKey().contains("netPayable")) {
							Double netPayable = earnings - deductions;
							entry.setValue(netPayable);
							logger.info("Net Payable " + entry.getValue());
							payRunEmployeePaymentVo.setAmount(netPayable);
						}

						if (entry.getKey().contains("status")) {
							String str = entry.getValue().toString();
							logger.info("status " + str);
							if (str.equalsIgnoreCase("NEW")) {
								entry.setValue(CommonConstants.STATUS_AS_ACTIVE);
								logger.info(entry.getValue().toString());

							} else if (str.equalsIgnoreCase(CommonConstants.STATUS_AS_DELETE)
									|| str.equalsIgnoreCase(CommonConstants.STATUS_AS_INACTIVE)) {

								entry.setValue(CommonConstants.STATUS_AS_DELETE);
								logger.info(entry.getValue().toString());
							} else if (str == null || str.trim().isEmpty()) {
								entry.setValue(CommonConstants.STATUS_AS_DELETE);

							}
						}

					}
					if (entry.getKey().contains("netPayable")) {
						Double amount = Double.parseDouble(String.valueOf(entry.getValue()));
						payRunAmount += amount;
						logger.info("payrun amount--- " + payRunAmount);
					}
				

				}
				
				logger.info("Total earnings -- " + earnings + "Total deductions -- " + deductions + "---for employee---"
						+ employeeId);
				employeePayRunList.add(payRunEmployeePaymentVo);
			}
			payRunVo.setPayRunAmount(BigDecimal.valueOf(payRunAmount).setScale(2, RoundingMode.CEILING).toString());
			payRunVo.setEmployeeCount(employeeList.size());
			logger.info("final map " + payrunMapList);
			String jsn2 = mapper.writeValueAsString(payrunMapList);
			logger.info("final string " + jsn2);
			preparedStatementrun.setString(11, jsn2);
			String settingsData = mapper.writeValueAsString(payRunVo.getSettingsData());
			logger.info("Settings data " + settingsData);
			preparedStatementrun.setString(12, settingsData);
			preparedStatementrun.setInt(13, payRunVo.getCurrencyId() != null ? payRunVo.getCurrencyId() : null);
			int rowAffected = preparedStatementrun.executeUpdate();
			if (rowAffected == 1) {
				try (ResultSet rs = preparedStatementrun.getGeneratedKeys()) {
					while (rs.next()) {
						payRunVo.setPayRunId(rs.getInt(1));
					}
				}
			}

			logger.info("createPayRunData Completed and the id is " + payRunVo.getPayRunId());
			logger.info("PayRunData count " + payRunVo.getPayRunInformation());

			if (employeePayRunList.size() > 0)
				createPayRunEmployee(employeePayRunList);

		} catch (Exception e) {
			logger.info("Error in createPayRunData ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatementrun, null);
		}

		return payRunVo;
	}

	private boolean checkPayRunReferenceExistForAnOrganization(Integer orgId, Connection con, String payRunReference)
			throws ApplicationException {
		Boolean isPayRunRefExist = false;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = con.prepareStatement(PayRunConstants.CHECK_PAY_RUN_REF_EXIST_FOR_ORG);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, payRunReference);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				isPayRunRefExist = true;
			}
			logger.info("checkPayRunReferenceExistForAnOrganization " + isPayRunRefExist);
		} catch (Exception e) {
			logger.info("Error in checkPayRunReferenceExistForAnOrganization  ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return isPayRunRefExist;
	}

	public PayRunVo getPayRunDataById( Integer payRunId)
			throws ApplicationException {
		logger.info("To get Pay run data payRunid " + payRunId);

		PayRunVo payRunVo = null;
		if (payRunId != null) {
			Connection conn = null;
			try {
				conn = getAccountsPayable();
				payRunVo = new PayRunVo();
				payRunVo = getPayRunData( payRunId, conn);
				List<UploadFileVo> uploadFileVos = new ArrayList<UploadFileVo>();

				for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(payRunVo.getPayRunId(),
						AttachmentsConstants.MODULE_TYPE_PAYROLL_PAYRUN)) {

					UploadFileVo uploadFileVo = new UploadFileVo();
					uploadFileVo.setId(attachments.getId());
					// uploadFileVo.setData(attachments.getD);
					uploadFileVo.setName(attachments.getFileName());
					uploadFileVo.setSize(attachments.getSize());
					uploadFileVos.add(uploadFileVo);
					logger.info("get Payrun attachment " + uploadFileVo);
				}
				payRunVo.setAttachments(uploadFileVos);
			} catch (Exception e) {
				logger.info("Error in getPayRunDataById ", e);
				throw new ApplicationException(e);
			} finally {
				closeResources(null, null, conn);
			}

		} else {
			throw new ApplicationException("Org Id , UserId , roleName and PayrunId are null");
		}

		return payRunVo;
	}

	private PayRunVo getPayRunData(Integer payRunId, Connection conn)
			throws ApplicationException {

		PayRunVo payRunVo = new PayRunVo();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		PreparedStatement preparedStatementinfo = null;
		ResultSet rsinfo = null;
		// List<PayRunInformationVo> payRunInformationVos = new
		// ArrayList<PayRunInformationVo>();
		// PayRunInformationVo payRunInformationVo = null;
		String payrunref = null;
		String[] payrunrefTemp = null;

		try {
			preparedStatement = conn.prepareStatement(PayRunConstants.SELECT_PAY_RUN, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, payRunId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(3, CommonConstants.STATUS_AS_DRAFT);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_PAID);
			preparedStatement.setString(5, CommonConstants.STATUS_AS_PARTIALLY_PAID);
			preparedStatement.setString(6, CommonConstants.STATUS_AS_DELETE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				payRunVo.setPayRunId(rs.getInt(1));
				payrunref = rs.getString(2);
				if (payrunref.contains("~")) {
					payrunrefTemp = rs.getString(2).split("~");
					logger.info("1::" + payrunrefTemp[0] + "2::" + payrunrefTemp[1]);

					if (payrunrefTemp.length > 1) {
						payRunVo.setPayRunRefPrefix(payrunrefTemp[0]);
						payRunVo.setPayrunReference(payrunrefTemp[1]);
						payRunVo.setPayRunRefSuffix(payrunrefTemp[2]);
					}
				} else {
					payRunVo.setPayrunReference(payrunref);
				}

				payRunVo.setPayrunDate(rs.getString(3)!=null ? DateConverter.getInstance().getDatePickerDateFormat(Date.valueOf(rs.getString(3))) : null);
				payRunVo.setPayPeriod(rs.getString(4));
				payRunVo.setStatus(rs.getString(5));
				payRunVo.setUserId(rs.getString(6));
				payRunVo.setOrgId(rs.getInt(7));
				payRunVo.setRoleName(rs.getString(8));
				payRunVo.setCopyPreviousPayRun(rs.getString(9));

				payRunVo.setPaymentCycle(rs.getString(10));
				ObjectMapper mapper = new ObjectMapper();
				String json = rs.getString(11);
				if (json != null) {
					GeneralLedgerVo gldata = mapper.readValue(json, GeneralLedgerVo.class);
					logger.info("Json map " + gldata);
					payRunVo.setGeneralLedgerData(gldata);

				}
				String json2 = rs.getString(12);
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> payrunMapList = mapper.readValue(json2, ArrayList.class);
				String employeeId = null;
				Double payRunAmount = 0.0;
				List<String> employeeList = new ArrayList<>();

				for (Map<String, Object> map : payrunMapList) {

					for (Map.Entry<String, Object> entry : map.entrySet()) {

						if (entry.getKey().contains("employeeId")) {
							employeeId = String.valueOf(entry.getValue());
							employeeList.add(employeeId);
							logger.info("pay run object employee--- " + employeeId);
						}

						if (employeeList.contains(employeeId)) {
							if (entry.getKey().contains("netPayable")) {
								logger.info("netPayable --" + entry.getKey() + "::" + entry.getValue());
								payRunAmount += Double.parseDouble(entry.getValue().toString());
							}

						}

					}

				}
				logger.info("payRunAmount " + payRunAmount);
				logger.info("Employee count :" + employeeList.size());
				payRunVo.setEmployeeCount(employeeList.size());

				String jsn2 = mapper.writeValueAsString(payrunMapList);
				logger.info("final string " + jsn2);
				if (json2 != null) {
					JsonNode readTree = mapper.readTree(json2);
					logger.info("json node " + readTree);
					payRunVo.setPayRunInformation(readTree);
				}
				String settingsData = rs.getString(13);
				logger.info("Settings data list " + settingsData);
				List<PayRunTableVo> payRunTableVos = new ArrayList<>();
				if (settingsData != null && settingsData.length() > 4) {
					JSONParser parser = new JSONParser();
					JSONArray columnsArray = (JSONArray) parser.parse(settingsData);
					for (Object obj : columnsArray) {
						JSONObject columnsObj = (JSONObject) obj;
						PayRunTableVo payRunTableVo = new PayRunTableVo();
						payRunTableVo.setId(
								columnsObj.get("id") != null ? Integer.parseInt(columnsObj.get("id").toString()) : 0);
						payRunTableVo
								.setName(columnsObj.get("name") != null ? columnsObj.get("name").toString() : null);
						payRunTableVo
								.setValue(columnsObj.get("value") != null ? columnsObj.get("value").toString() : null);
						payRunTableVo.setPayType(
								columnsObj.get("payType") != null ? columnsObj.get("payType").toString() : null);
						payRunTableVo.setIsShowColumn(columnsObj.get("isShowColumn") != null
								? Boolean.parseBoolean(columnsObj.get("isShowColumn").toString())
								: null);
						payRunTableVo.setFinalName(
								columnsObj.get("finalName") != null ? columnsObj.get("finalName").toString() : null);
						payRunTableVos.add(payRunTableVo);
					}
				}
				logger.info("Settings data final list " + payRunTableVos);
				payRunVo.setSettingsData(payRunTableVos);

				CurrencyVo currencyVo = currencyDao.getCurrency(rs.getInt(14));
				payRunVo.setCurrencyId(rs.getInt(14));
				payRunVo.setCreateTs(rs.getDate(15));

				logger.info("currencyVo "+currencyVo);
				 if( payRunAmount != null && currencyVo != null ) {
					String finalPayRunAmount = (currencyVo.getSymbol() != null ? currencyVo.getSymbol() : "") + " "+BigDecimal.valueOf(payRunAmount).setScale(2, RoundingMode.CEILING).toString();
					logger.info("finalPayRunAmount "+finalPayRunAmount);

					payRunVo.setPayRunAmount(finalPayRunAmount);
				} else {
					payRunVo.setPayRunAmount(BigDecimal.valueOf(payRunAmount).toString());
				}

			}

		} catch (Exception e) {
			logger.info("Error in getPayRunData ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
			closeResources(rsinfo, preparedStatementinfo, conn);
		}

		return payRunVo;
	}

	public List<PayRunVo> getAllPayRun(Integer orgId, String userId, String roleName) throws ApplicationException {

		logger.info("To get Pay run data for the org " + orgId + " and userid " + userId + " and roleName " + roleName);
		List<PayRunVo> payRunVos = new ArrayList<PayRunVo>();

		if (orgId != null && userId != null && roleName != null) {
			payRunVos = getAllPayRunData(orgId, userId, roleName);
			payRunVos.forEach(info -> {
				List<UploadFileVo> uploadFileVos = new ArrayList<UploadFileVo>();

				try {
					for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(info.getPayRunId(),
							AttachmentsConstants.MODULE_TYPE_PAYROLL_PAYRUN)) {

						UploadFileVo uploadFileVo = new UploadFileVo();
						uploadFileVo.setId(attachments.getId());
						// uploadFileVo.setData(attachments.getD);
						uploadFileVo.setName(attachments.getFileName());
						uploadFileVo.setSize(attachments.getSize());
						uploadFileVos.add(uploadFileVo);
						logger.info("get Payrun attachment " + uploadFileVo);
					}
				} catch (ApplicationException e) {

					throw new ApplicationRuntimeException(e);
				}
				info.setAttachments(uploadFileVos);
			});
		}
		/*
		 * else { throw new
		 * ApplicationException("Org Id , UserId , RoleName and PayRunId are null"); }
		 */

		return payRunVos;

	}

	private List<PayRunVo> getAllPayRunData(Integer orgId, String userId, String roleName) throws ApplicationException {

		logger.info(
				"To get getPayRunDataAll for the org " + orgId + " and userid " + userId + " and roleName " + roleName);

		List<PayRunVo> payRunVos = new ArrayList<PayRunVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = getAccountsPayable();

			if (roleName.equals("Super Admin")) {
				preparedStatement = conn.prepareStatement(PayRunConstants.SELECT_PAY_RUN_ALL_ORGANIZATION,
						Statement.RETURN_GENERATED_KEYS);
			} else {
				preparedStatement = conn.prepareStatement(PayRunConstants.SELECT_PAY_RUN_USER_ROLE,
						Statement.RETURN_GENERATED_KEYS);
			}

			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(3, CommonConstants.STATUS_AS_DRAFT);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_PAID);
			preparedStatement.setString(5, CommonConstants.STATUS_AS_PARTIALLY_PAID);
			if (!(roleName.equals("Super Admin"))) {
				preparedStatement.setString(3, userId);
				preparedStatement.setString(4, roleName);
				preparedStatement.setString(5, CommonConstants.STATUS_AS_ACTIVE);
				preparedStatement.setString(6, CommonConstants.STATUS_AS_DRAFT);
				preparedStatement.setString(7, CommonConstants.STATUS_AS_PAID);
				preparedStatement.setString(8, CommonConstants.STATUS_AS_PARTIALLY_PAID);
			}

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PayRunVo payRunVo = new PayRunVo();
				Integer payRunId = rs.getInt(1);
				payRunVo.setPayRunId(payRunId);
				String payrefNum = rs.getString(2) != null ? rs.getString(2).replace("~", "/") : null;
				payRunVo.setPayrunReference(payrefNum);
				payRunVo.setPayrunDate(rs.getString(3));
				payRunVo.setPayPeriod(rs.getString(4));
				payRunVo.setStatus(getDisplayStatus(rs.getString(5)));
				payRunVo.setUserId(rs.getString(6));
				payRunVo.setOrgId(rs.getInt(7));
				payRunVo.setRoleName(rs.getString(8));
				payRunVo.setCopyPreviousPayRun(rs.getString(9));
				payRunVo.setPayRunPaidAmount(null);
				payRunVo.setPaymentCycle(rs.getString(10));
				String json2 = rs.getString(11);
				ObjectMapper mapper = new ObjectMapper();
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> payrunMapList = mapper.readValue(json2, ArrayList.class);
				String employeeId = null;
				Double payRunAmount = 0.0;
				List<String> employeeList = new ArrayList<>();

				for (Map<String, Object> map : payrunMapList) {

					for (Map.Entry<String, Object> entry : map.entrySet()) {

						if (entry.getKey().contains("employeeId")) {
							employeeId = String.valueOf(entry.getValue());
							employeeList.add(employeeId);
							logger.info("pay run object employee--- " + employeeId);
						}

						if (employeeList.contains(employeeId)) {
							if (entry.getKey().contains("netPayable")) {
								logger.info("netPayable --" + entry.getKey() + "::" + entry.getValue());
								payRunAmount += Double.parseDouble(entry.getValue().toString());
//								 Double.parseDouble( entry.getValue().toString());
							}

						}

					}

				}
				logger.info("payRunAmount -all " + payRunAmount);
				logger.info("Employee count - all :" + employeeList.size());
				payRunVo.setEmployeeCount(employeeList.size());
				if (json2 != null) {
					// ObjectMapper mapper = new ObjectMapper();
					JsonNode readTree = mapper.readTree(json2);
					logger.info("json node " + readTree);
					payRunVo.setPayRunInformation(readTree);
				}
				String settingsData = rs.getString(12);
				logger.info("Settings data list " + settingsData);
				List<PayRunTableVo> payRunTableVos = new ArrayList<>();
				if (settingsData != null && settingsData.length() > 4) {
					JSONParser parser = new JSONParser();
					JSONArray columnsArray = (JSONArray) parser.parse(settingsData);
					for (Object obj : columnsArray) {
						JSONObject columnsObj = (JSONObject) obj;

						PayRunTableVo payRunTableVo = new PayRunTableVo();
						payRunTableVo.setId(
								columnsObj.get("id") != null ? Integer.parseInt(columnsObj.get("id").toString()) : 0);
						payRunTableVo
								.setName(columnsObj.get("name") != null ? columnsObj.get("name").toString() : null);
						payRunTableVo
								.setValue(columnsObj.get("value") != null ? columnsObj.get("value").toString() : null);
						payRunTableVo.setPayType(
								columnsObj.get("payType") != null ? columnsObj.get("payType").toString() : null);
						payRunTableVo.setIsShowColumn(columnsObj.get("isShowColumn") != null
								? Boolean.parseBoolean(columnsObj.get("isShowColumn").toString())
								: null);
						payRunTableVo.setFinalName(
								columnsObj.get("finalName") != null ? columnsObj.get("finalName").toString() : null);
						payRunTableVos.add(payRunTableVo);
					}
				}
				logger.info("Settings data final list " + payRunTableVos);
				payRunVo.setSettingsData(payRunTableVos);
				CurrencyVo currencyVo = currencyDao.getCurrency(rs.getInt(13));
				payRunVo.setCurrencyId(rs.getInt(13));

				 if(payRunAmount != null && currencyVo != null ) {
					String finalPayRunAmount = (currencyVo.getSymbol() != null ? currencyVo.getSymbol() : "") + " "+BigDecimal.valueOf(payRunAmount).setScale(2, RoundingMode.CEILING).toString();

					payRunVo.setPayRunAmount(finalPayRunAmount);
					payRunVo.setPayRunPaidAmount(getPayRunPaidAmount(payRunId, conn));
				 } else {
					 payRunVo.setPayRunAmount(BigDecimal.valueOf(payRunAmount).setScale(2, RoundingMode.CEILING).toString());
					 payRunVo.setPayRunPaidAmount(getPayRunPaidAmount(payRunId, conn));
				 }

			payRunVos.add(payRunVo);
				logger.info("getPayRunDataAll " + payRunVo);
			}

			logger.info("getPayRunDataAll2 " + payRunVos);
		} catch (Exception e) {
			logger.info("Error in getPayRunDataAll ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, conn);
		}
		return payRunVos;
	}
	
	private BigDecimal getPayRunPaidAmount(Integer payRunId,Connection conn) throws ApplicationException {
		logger.info("Entry into getPayRunPaidAmount:");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		BigDecimal payrunAdjustments = new BigDecimal("0");
		try {
			if (payRunId != null) {
				int currencyId = 0;
				String status="";
				preparedStatement = conn.prepareStatement(PayRunConstants.GET_BASIC_PAYRUN);
				preparedStatement.setInt(1, payRunId);
				rs = preparedStatement.executeQuery();
				while (rs.next()) {
					currencyId = rs.getInt(1);
					status=rs.getString(2);
				}
				if (currencyId > 0) {
					closeResources(rs, preparedStatement, null);
					preparedStatement = conn.prepareStatement(PayRunConstants.SELECT_ALL_EMPLOYEES_BY_PAYRUN_ID);
					preparedStatement.setInt(1, payRunId);
					rs = preparedStatement.executeQuery();
					BigDecimal payrunTotal = new BigDecimal("0");
					
					while (rs.next()) {
						int payRunEmpId = rs.getInt(1);
						int empId = rs.getInt(2);
						BigDecimal total = new BigDecimal("0");
						BigDecimal due = new BigDecimal("0");
						if (rs.getString(3) != null) {
							total = rs.getBigDecimal(3);
							total = total.setScale(2, BigDecimal.ROUND_HALF_EVEN);
						}
						payrunTotal=payrunTotal.add(total);
						BigDecimal adjustments = getTotalAdjustmentsForEmployeePayrunCurrency(payRunId, empId,
								currencyId);
						payrunAdjustments=payrunAdjustments.add(adjustments);

					}
				}
			}
		} catch (Exception e) {
			logger.info("Error in updatePayRunBalance:: ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return payrunAdjustments;
	}
	
	private BigDecimal getTotalAdjustmentsForEmployeePayrunCurrency(int payRunId,int empId,int currencyId) throws ApplicationException {
		logger.info("Entry into in getTotalAdjustmentsForEmployeePayrunCurrency");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		BigDecimal total = new BigDecimal("0");
		try {
			// Get Total Amount Of VednorAdv Id From all Bill Payments
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(PayRunConstants.GET_ADJ_PAYRUN_FOR_EMP_CURRENCY);
			preparedStatement.setInt(1, payRunId);
			preparedStatement.setInt(2, empId);
			preparedStatement.setInt(3, currencyId);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(5, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			logger.info(preparedStatement.toString());
			while (rs.next()) {
				if (rs.getString(1) != null) {
					total = rs.getBigDecimal(1);
					total = total.setScale(2, BigDecimal.ROUND_HALF_EVEN);
				}
			}

			logger.info("getTotalAdjustmentsForEmployeePayrunCurrency : Fetched total amount:" + total
					+ ",For  payRunId:" + payRunId+" payRunId:" +empId+","+currencyId);
		} catch (Exception e) {
			logger.error("Error in getTotalAdjustmentsForEmployeePayrunCurrency", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return total;

	}

	private String getDisplayStatus(String string) {
		String displayStatus = null;
		switch (string) {
		case CommonConstants.STATUS_AS_ACTIVE:

			// displayStatus = CommonConstants.DISPLAY_STATUS_AS_ACTIVE; commented as per
			// FIN779
			displayStatus = CommonConstants.DISPLAY_STATUS_AS_OPEN;
			break;
		case CommonConstants.STATUS_AS_VOID:
			displayStatus = CommonConstants.DISPLAY_STATUS_AS_VOID;
			break;
		case CommonConstants.STATUS_AS_DRAFT:
			displayStatus = CommonConstants.DISPLAY_STATUS_AS_DRAFT;
			break;
		case CommonConstants.STATUS_AS_INACTIVE:
			displayStatus = CommonConstants.DISPLAY_STATUS_AS_INACTIVE;
			break;
		case CommonConstants.STATUS_AS_ACCEPT:
			displayStatus = CommonConstants.DISPLAY_STATUS_AS_ACCEPT;
			break;
		case CommonConstants.STATUS_AS_DECLINE:
			displayStatus = CommonConstants.DISPLAY_STATUS_AS_DECLINE;
			break;
		case CommonConstants.STATUS_AS_EXPIRED:
			displayStatus = CommonConstants.DISPLAY_STATUS_AS_EXPIRED;
			break;
		case CommonConstants.STATUS_AS_OPEN:
			displayStatus = CommonConstants.DISPLAY_STATUS_AS_OPEN;
			break;
		case CommonConstants.STATUS_AS_PAID:
			displayStatus = CommonConstants.DISPLAY_STATUS_AS_PAID;
			break;
		case CommonConstants.STATUS_AS_UNPAID:
			displayStatus = CommonConstants.DISPLAY_STATUS_AS_UNPAID;
			break;
		case CommonConstants.STATUS_AS_OVERDUE:
			displayStatus = CommonConstants.DISPLAY_STATUS_AS_OVERDUE;
			break;
		case CommonConstants.STATUS_AS_PARTIALLY_PAID:
			displayStatus = CommonConstants.DISPLAY_STATUS_AS_PARTIALLY_PAID;
			break;

		}
		return displayStatus;
	}

	/*
	 * private BigDecimal getPayRunAmount(Integer payRunId) throws
	 * ApplicationException { PreparedStatement preparedStatementinfo =null;
	 * ResultSet rsinfo =null; Connection conn = null; BigDecimal payRunAmount =
	 * null; try { conn = getAccountsPayable(); preparedStatementinfo =
	 * conn.prepareStatement(PayRunConstants.GET_PAY_AMOUNT,Statement.
	 * RETURN_GENERATED_KEYS); preparedStatementinfo.setInt(1, payRunId);
	 * preparedStatementinfo.setString(2, CommonConstants.STATUS_AS_ACTIVE);
	 * preparedStatementinfo.setString(3, CommonConstants.STATUS_AS_DRAFT);
	 * preparedStatementinfo.setString(4, CommonConstants.STATUS_AS_PAID);
	 * preparedStatementinfo.setString(5, CommonConstants.STATUS_AS_PARTIALLY_PAID);
	 * rsinfo = preparedStatementinfo.executeQuery(); while (rsinfo.next()) {
	 * payRunAmount = rsinfo.getBigDecimal(1); } }catch (Exception e) {
	 * logger.info("Error in getPayRunAmount ", e); throw new
	 * ApplicationException(e); } finally {
	 * closeResources(rsinfo,preparedStatementinfo,conn); }
	 * 
	 * return payRunAmount; }
	 * 
	 * private Integer getEmployeeCount(Integer payRunId) throws
	 * ApplicationException { PreparedStatement preparedStatementinfo =null;
	 * ResultSet rsinfo =null; Connection conn = null; Integer employeeCount = null;
	 * try { conn = getAccountsPayable(); preparedStatementinfo =
	 * conn.prepareStatement(PayRunConstants.GET_EMPLOYEE_COUNT,Statement.
	 * RETURN_GENERATED_KEYS); preparedStatementinfo.setInt(1, payRunId);
	 * preparedStatementinfo.setString(2, CommonConstants.STATUS_AS_ACTIVE);
	 * preparedStatementinfo.setString(3, CommonConstants.STATUS_AS_DRAFT);
	 * preparedStatementinfo.setString(4, CommonConstants.STATUS_AS_PAID);
	 * preparedStatementinfo.setString(5, CommonConstants.STATUS_AS_PARTIALLY_PAID);
	 * rsinfo = preparedStatementinfo.executeQuery(); while (rsinfo.next()) {
	 * employeeCount = rsinfo.getInt(1); } } catch (Exception e) {
	 * logger.info("Error in getEmployeeCount ", e); throw new
	 * ApplicationException(e); } finally {
	 * closeResources(rsinfo,preparedStatementinfo,conn); } return employeeCount;
	 * 
	 * }
	 */

	public PayRunVo updatePayRun(PayRunVo payRunVo) throws Exception {
		logger.info("To update Pay run" + payRunVo);
		Connection connection = null;
		if (payRunVo.getOrgId() != null && payRunVo.getUserId() != null && payRunVo.getPayRunId() != null) {
			try {
				connection = getAccountsPayable();
				connection.setAutoCommit(false);
				updatePayRunData(payRunVo, connection);
				if (payRunVo.getAttachments() != null && payRunVo.getAttachments().size() > 0) {
					attachmentsDao.createAttachments(payRunVo.getOrgId(), payRunVo.getUserId(),
							payRunVo.getAttachments(), AttachmentsConstants.MODULE_TYPE_PAYROLL_PAYRUN,
							payRunVo.getPayRunId(), payRunVo.getRoleName());

				}
				if (payRunVo.getAttachmentsToRemove() != null) {
					logger.info("Attachment update 1");
					for (Integer attachmentId : payRunVo.getAttachmentsToRemove()) {
						logger.info("Attachment update 2");
						attachmentsDao.changeStatusForSingleAttachment(attachmentId, CommonConstants.STATUS_AS_DELETE,
								payRunVo.getUserId(), payRunVo.getRoleName());
					}
				}
				connection.commit();
				logger.info("Succecssfully updated Pay run in PayRunDao" + payRunVo);
			} catch (Exception e) {
				logger.info("Error in Pay Run update :: ", e);
				connection.rollback();
				throw new ApplicationException(e);
			} finally {
				closeResources(null, null, connection);
			}
		} else {
			throw new Exception("No Data for PayRun Organization and User Id");
		}
		return payRunVo;
	}

	private PayRunVo updatePayRunData(PayRunVo payRunVo, Connection connection) throws ApplicationException {

		PreparedStatement preparedStatementrun = null;

		boolean isPayRunIdExists = checkPayRunIdExists(payRunVo.getPayRunId(), payRunVo.getOrgId(), connection);

		if (!isPayRunIdExists) {
			throw new ApplicationException("PayRun Id not exist for the organization");
		}

		String payRunReference = null;
		if (!payRunVo.getPayRunRefPrefix().isEmpty() && !payRunVo.getPayRunRefSuffix().isEmpty()) {
			StringBuilder payRunReferenceBuilder = new StringBuilder();
			payRunReferenceBuilder
					.append(payRunVo.getPayRunRefPrefix() != null && !payRunVo.getPayRunRefPrefix().isEmpty()
							? payRunVo.getPayRunRefPrefix()
							: " ")
					.append("~").append(payRunVo.getPayrunReference()).append("~")
					.append(payRunVo.getPayRunRefSuffix() != null && !payRunVo.getPayRunRefSuffix().isEmpty()
							? payRunVo.getPayRunRefSuffix()
							: " ");
			payRunReference = payRunReferenceBuilder.toString();
		}
		if (payRunVo.getPayRunRefPrefix() != null && payRunVo.getPayRunRefSuffix().isEmpty()) {
			StringBuilder payRunReferenceBuilder = new StringBuilder();
			payRunReferenceBuilder
					.append(payRunVo.getPayRunRefPrefix() != null && !payRunVo.getPayRunRefPrefix().isEmpty()
							? payRunVo.getPayRunRefPrefix()
							: " ")
					.append("~").append(payRunVo.getPayrunReference());
			payRunReference = payRunReferenceBuilder.toString();
		}
		if (payRunVo.getPayRunRefPrefix().isEmpty() && payRunVo.getPayRunRefSuffix() != null) {
			StringBuilder payRunReferenceBuilder = new StringBuilder();
			payRunReferenceBuilder.append(payRunVo.getPayrunReference()).append("~")
					.append(payRunVo.getPayRunRefSuffix() != null && !payRunVo.getPayRunRefSuffix().isEmpty()
							? payRunVo.getPayRunRefSuffix()
							: " ");
			payRunReference = payRunReferenceBuilder.toString();
		}

		if (payRunVo.getPayRunRefPrefix().isEmpty() && payRunVo.getPayRunRefSuffix().isEmpty()) {
			payRunReference = payRunVo.getPayrunReference();
		}

		boolean isPayRunReferenceExist = checkPayRunReferenceExistForUpdate(payRunVo.getPayRunId(), connection,
				payRunReference);
		if (isPayRunReferenceExist) {
			throw new ApplicationException("PayRun reference already exist for the organization");
		}

		try {
			preparedStatementrun = connection.prepareStatement(PayRunConstants.UPDATE_PAY_RUN,
					Statement.RETURN_GENERATED_KEYS);
			preparedStatementrun.setString(1, payRunReference != null ? payRunReference : null);
			preparedStatementrun.setString(2, payRunVo.getPayrunDate() != null ? DateConverter.getInstance().correctDatePickerDateToString(payRunVo.getPayrunDate()) : null);
			preparedStatementrun.setString(3, payRunVo.getPayPeriod() != null ? payRunVo.getPayPeriod() : null);
			preparedStatementrun.setString(4, payRunVo.getStatus() != null ? payRunVo.getStatus() : null);
			preparedStatementrun.setString(5, payRunVo.getUserId() != null ? payRunVo.getUserId() : null);
			preparedStatementrun.setLong(6, payRunVo.getOrgId() != null ? payRunVo.getOrgId() : 0);
			preparedStatementrun.setString(7, payRunVo.getRoleName() != null ? payRunVo.getRoleName() : null);
			preparedStatementrun.setString(8,
					payRunVo.getCopyPreviousPayRun() != null ? payRunVo.getCopyPreviousPayRun() : null);
			preparedStatementrun.setString(9, payRunVo.getPaymentCycle() != null ? payRunVo.getPaymentCycle() : null);
			ObjectMapper mapper = new ObjectMapper();
			String glData = payRunVo.getGeneralLedgerData() != null
					? mapper.writeValueAsString(payRunVo.getGeneralLedgerData())
					: null;
			logger.info("glData>> " + glData);
			preparedStatementrun.setString(10, glData);
			String jsn = mapper.writeValueAsString(payRunVo.getPayRunInformation());
			logger.info("Insert info " + jsn);
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> payrunMapList = mapper.readValue(jsn, ArrayList.class);
			String employeeId = null;
			Double earnings = 0.0;
			Double deductions = 0.0;
			List<String> employeeList = new ArrayList<>();

			List<PayRunEmployeePaymentVo> employeePayRunList = new LinkedList<PayRunEmployeePaymentVo>();

			for (Map<String, Object> map : payrunMapList) {
				earnings = 0.0;
				deductions = 0.0;
				PayRunEmployeePaymentVo payRunEmployeePaymentVo = new PayRunEmployeePaymentVo();
				for (Map.Entry<String, Object> entry : map.entrySet()) {					
					payRunEmployeePaymentVo.setPayrunReference(payRunReference);
					if (entry.getKey().contains("employeeId")) {
						employeeId = String.valueOf(entry.getValue());
						employeeList.add(employeeId);
						logger.info("pay run object employee--- " + employeeId);
						payRunEmployeePaymentVo.setEmpId(employeeId);
					}

					if (employeeList.contains(employeeId)) {
						if (entry.getKey().contains("earnings")) {
							logger.info("earnings--" + entry.getKey() + "::" + entry.getValue());
							earnings += Double.parseDouble(entry.getValue().toString());
						} else if (entry.getKey().contains("deductions")) {
							logger.info("deductions--" + entry.getKey() + "::" + entry.getValue());
							deductions += Double.parseDouble(entry.getValue().toString());
						}
						if (entry.getKey().contains("totalEarnings")) {

							entry.setValue(earnings);
							logger.info("Total - earnings " + entry.getValue());
						}
						if (entry.getKey().contains("totalDeductions")) {

							entry.setValue(deductions);
							logger.info("Total - Deductions " + entry.getValue());
						}
						if (entry.getKey().contains("netPayable")) {
							Double netPayable = earnings - deductions;
							entry.setValue(netPayable);
							logger.info("Net Payable " + entry.getValue());
							payRunEmployeePaymentVo.setAmount(netPayable);
						}
						if (entry.getKey().contains("status")) {
							String str = entry.getValue().toString();
							logger.info("status " + str);
							if (str.equalsIgnoreCase("NEW")) {
								entry.setValue(CommonConstants.STATUS_AS_ACTIVE);
								logger.info(entry.getValue().toString());

							} else if (str.equalsIgnoreCase(CommonConstants.STATUS_AS_DELETE)
									|| str.equalsIgnoreCase(CommonConstants.STATUS_AS_INACTIVE)) {

								entry.setValue(CommonConstants.STATUS_AS_DELETE);
								logger.info(entry.getValue().toString());
							} else if (str == null || str.trim().isEmpty()) {
								entry.setValue(CommonConstants.STATUS_AS_DELETE);

							}
						}

					}
					

				}
				logger.info("Total earnings -- " + earnings + "Total deductions -- " + deductions + "---for employee---"
						+ employeeId);
				employeePayRunList.add(payRunEmployeePaymentVo);

			}
			logger.info("final map " + payrunMapList);
			String jsn2 = mapper.writeValueAsString(payrunMapList);
			logger.info("final string " + jsn2);
			preparedStatementrun.setString(11, jsn2);
			String settingsData = mapper.writeValueAsString(payRunVo.getSettingsData());
			logger.info("Settings data " + settingsData);
			preparedStatementrun.setString(12, settingsData);
			preparedStatementrun.setInt(13, payRunVo.getCurrencyId());
			preparedStatementrun.setInt(14, payRunVo.getPayRunId() != null ? payRunVo.getPayRunId() : 0);
			int rw = preparedStatementrun.executeUpdate();

			logger.info("updatePayRunData Completed and the id is " + rw);
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> payrunMapList2 = mapper.readValue(jsn2, ArrayList.class);
			// String employeeId2 = null;
			Double payRunAmount = 0.0;
			List<String> employeeList2 = new ArrayList<>();

			for (Map<String, Object> map : payrunMapList2) {

				for (Map.Entry<String, Object> entry : map.entrySet()) {

					if (entry.getKey().contains("employeeId")) {
						employeeId = String.valueOf(entry.getValue());
						employeeList2.add(employeeId);
						logger.info("pay run object employee--- " + employeeId);
					}

					if (employeeList2.contains(employeeId)) {
						if (entry.getKey().contains("netPayable")) {
							logger.info("netPayable --" + entry.getKey() + "::" + entry.getValue());
							payRunAmount += Double.parseDouble(entry.getValue().toString());

						}

					}

				}

			}
			logger.info("payRunAmount " + payRunAmount);
			logger.info("Employee count :" + employeeList2.size());
			payRunVo.setEmployeeCount(employeeList.size());
			payRunVo.setPayRunAmount(BigDecimal.valueOf(payRunAmount).setScale(2, RoundingMode.CEILING).toString());

			// List<Integer> payRunInfoIds = new ArrayList<Integer>();
			// payRunVo.getPayRunInformation().forEach(info -> {
			//
			// PreparedStatement preparedStatement;
			// try {
			// if(info.getStatus() != null ) {
			// switch(info.getStatus().toUpperCase()) {
			// case CommonConstants.STATUS_AS_NEW:
			// if(info.getEmployeeId() != null) {
			// BigDecimal totalEarnings = calculateTotalEarnings(info);
			// BigDecimal totalDeductions = calculateTotalDeductions(info);
			// BigDecimal netPayable = totalEarnings.subtract(totalDeductions);
			// BigDecimal totalPf = calculateTotalPf(info);
			// preparedStatement =
			// connection.prepareStatement(PayRunConstants.INSERT_INTO_PAY_RUN_INFO,
			// Statement.RETURN_GENERATED_KEYS);
			// preparedStatement.setInt(1, payRunVo.getPayRunId());
			// preparedStatement.setString(2,info.getEmployeeId() != null ?
			// info.getEmployeeId() : null);
			// preparedStatement.setString(3,info.getEmployeeName() != null ?
			// info.getEmployeeName() : null);
			// preparedStatement.setBigDecimal(4,info.getEarningsBasic() != null ?
			// info.getEarningsBasic() : null);
			// preparedStatement.setBigDecimal(5,info.getEarningsHRA() != null ?
			// info.getEarningsHRA() : null);
			// preparedStatement.setBigDecimal(6,info.getEarningsSpecialAllowance() != null
			// ? info.getEarningsSpecialAllowance() : null);
			// preparedStatement.setBigDecimal(7,info.getEarningsOvertime() != null ?
			// info.getEarningsOvertime() : null);
			// preparedStatement.setBigDecimal(8,info.getEarningsConveyance() != null ?
			// info.getEarningsConveyance() : null);
			// preparedStatement.setBigDecimal(9,info.getEarningsFuel() != null ?
			// info.getEarningsFuel() : null);
			// preparedStatement.setBigDecimal(10,info.getEarningsTelephone() != null ?
			// info.getEarningsTelephone() : null);
			// preparedStatement.setBigDecimal(11,info.getDeductionsIncomeTax() != null ?
			// info.getDeductionsIncomeTax() : null);
			// preparedStatement.setBigDecimal(12,info.getDeductionsProfessionalTax() !=
			// null ? info.getDeductionsProfessionalTax() : null);
			// preparedStatement.setBigDecimal(13,totalPf);
			// preparedStatement.setString(14,payRunVo.getStatus() != null ?
			// payRunVo.getStatus() : null);
			// preparedStatement.setBigDecimal(15, totalEarnings);
			// preparedStatement.setBigDecimal(16, totalDeductions);
			// preparedStatement.setBigDecimal(17, netPayable);
			// preparedStatement.setBigDecimal(18, info.getEarningsLeaveEncashment() != null
			// ?info.getEarningsLeaveEncashment() : null);
			// preparedStatement.setBigDecimal(19, info.getEarningsBonus() != null ?
			// info.getEarningsBonus() : null);
			// preparedStatement.setBigDecimal(20, info.getEarningsTravel()!= null ?
			// info.getEarningsTravel() : null);
			// preparedStatement.setBigDecimal(21,
			// info.getDeductionsPfEmployeeContribution() != null ?
			// info.getDeductionsPfEmployeeContribution() : null);
			// preparedStatement.setBigDecimal(22,
			// info.getDeductionsPfEmployerContribution() != null ?
			// info.getDeductionsPfEmployerContribution() : null);
			// preparedStatement.setBigDecimal(23,
			// info.getDeductionsEsiEmployeeContribution() != null ?
			// info.getDeductionsEsiEmployeeContribution() : null);
			// preparedStatement.setBigDecimal(24,
			// info.getDeductionsEsiEmployerContribution() != null ?
			// info.getDeductionsEsiEmployerContribution() : null);
			//
			// int rowAffected1 = preparedStatement.executeUpdate();
			// if (rowAffected1 == 1) {
			// ResultSet rs = preparedStatement.getGeneratedKeys();
			// while (rs.next()) {
			// info.setPayRunInfoId(rs.getInt(1));
			// } }
			// logger.info("PayRunDataInfo count for upd -insert "+ rowAffected1 );
			// } else {
			// throw new ApplicationException("Employee ID is missing");
			// }
			//
			// break;
			// case CommonConstants.STATUS_AS_ACTIVE:
			// BigDecimal totalEarningsAct = calculateTotalEarnings(info);
			// BigDecimal totalDeductionsAct = calculateTotalDeductions(info);
			// BigDecimal netPayableAct = totalEarningsAct.subtract(totalDeductionsAct);
			// BigDecimal totalPfAct = calculateTotalPf(info);
			// preparedStatement =
			// connection.prepareStatement(PayRunConstants.UPDATE_INTO_PAY_RUN_INFO,
			// Statement.RETURN_GENERATED_KEYS);
			// preparedStatement.setInt(1, payRunVo.getPayRunId());
			// preparedStatement.setString(2,info.getEmployeeId() != null ?
			// info.getEmployeeId() : null);
			// preparedStatement.setString(3,info.getEmployeeName() != null ?
			// info.getEmployeeName() : null);
			// preparedStatement.setBigDecimal(4,info.getEarningsBasic() != null ?
			// info.getEarningsBasic() : null);
			// preparedStatement.setBigDecimal(5,info.getEarningsHRA() != null ?
			// info.getEarningsHRA() : null);
			// preparedStatement.setBigDecimal(6,info.getEarningsSpecialAllowance() != null
			// ? info.getEarningsSpecialAllowance() : null);
			// preparedStatement.setBigDecimal(7,info.getEarningsOvertime() != null ?
			// info.getEarningsOvertime() : null);
			// preparedStatement.setBigDecimal(8,info.getEarningsConveyance() != null ?
			// info.getEarningsConveyance() : null);
			// preparedStatement.setBigDecimal(9,info.getEarningsFuel() != null ?
			// info.getEarningsFuel() : null);
			// preparedStatement.setBigDecimal(10, info.getEarningsTelephone() != null ?
			// info.getEarningsTelephone() : null);
			// preparedStatement.setBigDecimal(11,info.getDeductionsIncomeTax() != null ?
			// info.getDeductionsIncomeTax() : null);
			// preparedStatement.setBigDecimal(12, info.getDeductionsProfessionalTax() !=
			// null ?info.getDeductionsProfessionalTax() : null);
			// preparedStatement.setBigDecimal(13,totalPfAct);
			// preparedStatement.setString(14,info.getStatus() != null ? info.getStatus() :
			// null);
			// preparedStatement.setBigDecimal(15, totalEarningsAct);
			// preparedStatement.setBigDecimal(16, totalDeductionsAct);
			// preparedStatement.setBigDecimal(17, netPayableAct);
			// preparedStatement.setBigDecimal(18, info.getEarningsLeaveEncashment() != null
			// ?info.getEarningsLeaveEncashment() : null);
			// preparedStatement.setBigDecimal(19, info.getEarningsBonus() != null ?
			// info.getEarningsBonus() : null);
			// preparedStatement.setBigDecimal(20, info.getEarningsTravel()!= null ?
			// info.getEarningsTravel() : null);
			// preparedStatement.setBigDecimal(21,
			// info.getDeductionsPfEmployeeContribution() != null ?
			// info.getDeductionsPfEmployeeContribution() : null);
			// preparedStatement.setBigDecimal(22,
			// info.getDeductionsPfEmployerContribution() != null ?
			// info.getDeductionsPfEmployerContribution() : null);
			// preparedStatement.setBigDecimal(23,
			// info.getDeductionsEsiEmployeeContribution() != null ?
			// info.getDeductionsEsiEmployeeContribution() : null);
			// preparedStatement.setBigDecimal(24,
			// info.getDeductionsEsiEmployerContribution() != null ?
			// info.getDeductionsEsiEmployerContribution() : null);
			// preparedStatement.setInt(25,info.getPayRunInfoId() != null ?
			// info.getPayRunInfoId() : 0);
			// int rs1 = preparedStatement.executeUpdate();
			//
			//
			//
			// logger.info("PayRunDataInfo count for upd "+ rs1 );
			// break;
			// case CommonConstants.STATUS_AS_DRAFT:
			// BigDecimal totalEarningsDft = calculateTotalEarnings(info);
			// BigDecimal totalDeductionsDft = calculateTotalDeductions(info);
			// BigDecimal netPayableDft = totalEarningsDft.subtract(totalDeductionsDft);
			// BigDecimal totalPfDft = calculateTotalPf(info);
			// preparedStatement =
			// connection.prepareStatement(PayRunConstants.UPDATE_INTO_PAY_RUN_INFO,
			// Statement.RETURN_GENERATED_KEYS);
			// preparedStatement.setInt(1, payRunVo.getPayRunId());
			// preparedStatement.setString(2,info.getEmployeeId() != null ?
			// info.getEmployeeId() : null);
			// preparedStatement.setString(3,info.getEmployeeName() != null ?
			// info.getEmployeeName() : null);
			// preparedStatement.setBigDecimal(4,info.getEarningsBasic() != null ?
			// info.getEarningsBasic() : null);
			// preparedStatement.setBigDecimal(5,info.getEarningsHRA() != null ?
			// info.getEarningsHRA() : null);
			// preparedStatement.setBigDecimal(6,info.getEarningsSpecialAllowance() != null
			// ? info.getEarningsSpecialAllowance() : null);
			// preparedStatement.setBigDecimal(7,info.getEarningsOvertime() != null ?
			// info.getEarningsOvertime() : null);
			// preparedStatement.setBigDecimal(8,info.getEarningsConveyance() != null ?
			// info.getEarningsConveyance() : null);
			// preparedStatement.setBigDecimal(9,info.getEarningsFuel() != null ?
			// info.getEarningsFuel() : null);
			// preparedStatement.setBigDecimal(10, info.getEarningsTelephone() != null ?
			// info.getEarningsTelephone() : null);
			// preparedStatement.setBigDecimal(11,info.getDeductionsIncomeTax() != null ?
			// info.getDeductionsIncomeTax() : null);
			// preparedStatement.setBigDecimal(12, info.getDeductionsProfessionalTax() !=
			// null ?info.getDeductionsProfessionalTax() : null);
			// preparedStatement.setBigDecimal(13,totalPfDft);
			// preparedStatement.setString(14,info.getStatus() != null ? info.getStatus() :
			// null);
			// preparedStatement.setBigDecimal(15, totalEarningsDft);
			// preparedStatement.setBigDecimal(16, totalDeductionsDft);
			// preparedStatement.setBigDecimal(17, netPayableDft);
			// preparedStatement.setBigDecimal(18, info.getEarningsLeaveEncashment() != null
			// ?info.getEarningsLeaveEncashment() : null);
			// preparedStatement.setBigDecimal(19, info.getEarningsBonus() != null ?
			// info.getEarningsBonus() : null);
			// preparedStatement.setBigDecimal(20, info.getEarningsTravel()!= null ?
			// info.getEarningsTravel() : null);
			// preparedStatement.setBigDecimal(21,
			// info.getDeductionsPfEmployeeContribution() != null ?
			// info.getDeductionsPfEmployeeContribution() : null);
			// preparedStatement.setBigDecimal(22,
			// info.getDeductionsPfEmployerContribution() != null ?
			// info.getDeductionsPfEmployerContribution() : null);
			// preparedStatement.setBigDecimal(23,
			// info.getDeductionsEsiEmployeeContribution() != null ?
			// info.getDeductionsEsiEmployeeContribution() : null);
			// preparedStatement.setBigDecimal(24,
			// info.getDeductionsEsiEmployerContribution() != null ?
			// info.getDeductionsEsiEmployerContribution() : null);
			// preparedStatement.setInt(25,info.getPayRunInfoId() != null ?
			// info.getPayRunInfoId() : 0);
			// int rs3 = preparedStatement.executeUpdate();
			// logger.info("PayRunDataInfo count for upd "+ rs3 );
			// break;
			// case CommonConstants.STATUS_AS_DELETE:
			// preparedStatement =
			// connection.prepareStatement(PayRunConstants.UPDATE_INTO_PAY_RUN_INFO_DEL,
			// Statement.RETURN_GENERATED_KEYS);
			// preparedStatement.setString(1,info.getStatus() != null ? info.getStatus() :
			// null);
			// preparedStatement.setInt(2,info.getPayRunInfoId() != null ?
			// info.getPayRunInfoId() : 0);
			// int rs2 = preparedStatement.executeUpdate();
			// logger.info("PayRunDataInfo count for del "+ rs2 );
			//
			//
			// break;
			// }
			// }}
			// catch (Exception e) {
			// logger.info(e.getMessage());
			// }
			//
			// logger.info("updatePayRunData Completed and the ids are "+ payRunInfoIds );
			// });
			// Integer payRunId = payRunVo.getPayRunId();
			// payRunVo.setEmployeeCount(getEmployeeCount(payRunId));
			// payRunVo.setPayRunAmount(getPayRunAmount(payRunId));

			if (employeePayRunList.size() > 0)
				createPayRunEmployee(employeePayRunList);

		} catch (Exception e) {
			logger.info("Error in updatePayRunData - info ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatementrun, null);
		}
		return payRunVo;
	}

	private boolean checkPayRunReferenceExistForUpdate(Integer payRunId, Connection connection, String payrunReference)
			throws ApplicationException {
		Boolean isPayRunIdExists = false;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = connection.prepareStatement(PayRunConstants.CHECK_PAY_RUN_REF_EXIST_FOR_ORG_UPD);
			preparedStatement.setInt(1, payRunId);
			preparedStatement.setString(2, payrunReference);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				isPayRunIdExists = true;
			}
			logger.info("checkPayRunReferenceExistForUpdate " + isPayRunIdExists);
		} catch (Exception e) {
			logger.info("Error in isPayRunIdExists  ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return isPayRunIdExists;
	}

	private boolean checkPayRunIdExists(Integer payRunId, Integer orgId, Connection connection)
			throws ApplicationException {
		Boolean isPayRunIdExists = false;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = connection.prepareStatement(PayRunConstants.CHECK_PAY_RUN_ID_EXIST_FOR_ORG);
			preparedStatement.setInt(1, payRunId);
			preparedStatement.setInt(2, orgId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				isPayRunIdExists = true;
			}
			logger.info("checkPayRunIdExists " + isPayRunIdExists);
		} catch (Exception e) {
			logger.info("Error in isPayRunIdExists  ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return isPayRunIdExists;
	}

	public List<PaymentTypeVo> getPayRunDataForOrganization(Integer orgId, Connection conn)
			throws ApplicationException {

		logger.info("To get getPayRunDataAll for the org " + orgId);
		List<PaymentTypeVo> payRunVos = new ArrayList<PaymentTypeVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			preparedStatement = conn.prepareStatement(PayRunConstants.SELECT_PAY_RUN_ORG,
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(3, CommonConstants.STATUS_AS_DRAFT);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_PAID);
			preparedStatement.setString(5, CommonConstants.STATUS_AS_PARTIALLY_PAID);

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PaymentTypeVo payRunVo = new PaymentTypeVo();
				payRunVo.setId(rs.getInt(1));
				payRunVo.setValue(rs.getInt(1));
				payRunVo.setName(rs.getString(2));
				payRunVos.add(payRunVo);
				logger.info("getPayRunDataAll " + payRunVo);
			}
		} catch (Exception e) {
			logger.info("Error in getPayRunDataForOrganization - info ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return payRunVos;
	}

	public List<PayRunPeriodVo> getReportPeriod(Integer orgId, Connection conn) throws ApplicationException {
		List<PayRunPeriodVo> payRunPeriodVos = new ArrayList<>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		// List<Integer> payRunPeriodId = getPayRunPeriodId(orgId);
		try {
			preparedStatement = conn.prepareStatement(PayRollConstants.GET_PAY_RUN_PERIOD_FREQUENCY);
			// java.sql.Array array =
			// preparedStatement.getConnection().createArrayOf("Integer",
			// payRunPeriodId.toArray() );

			preparedStatement.setString(1, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setInt(2, orgId);
			preparedStatement.setString(3, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PayRunPeriodVo period = new PayRunPeriodVo();
				period.setId(rs.getString(1));
				period.setName(rs.getString(2));
				period.setValue(rs.getString(2));
				period.setPayRunPeriodCycle(rs.getString(3));
				period.setPayRunPeriodStartDate(rs.getString(4));
				period.setPayRunPeriodEndDate(rs.getString(5));
				period.setPayRunPeriodPayFrequencyType(rs.getString(6));
				payRunPeriodVos.add(period);
			}
			logger.info("Fetched in getReportPeriods:" + payRunPeriodVos);
		} catch (Exception e) {
			logger.info("Error in getReportPeriods", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}

		return payRunPeriodVos;
	}

	// private List<Integer> getPayRunPeriodId(Integer orgId) throws
	// ApplicationException {
	// logger.info("getPayRunPeriodId - OrgID "+ orgId);
	// List<Integer> payRunPeriodIds = new ArrayList<>();
	// Integer payRunPeriodId = null;
	// @SuppressWarnings("unused")
	// String period =null;
	// Connection con = null;
	// PreparedStatement preparedStatement = null;
	// ResultSet rs =null;
	// try{
	// con = getFinanceCommon();
	// preparedStatement =
	// con.prepareStatement(SettingsAndPreferencesConstants.GET_PAY_RUN_PERIOD_FREQUENCY_ID_FOR_ORG);
	// preparedStatement.setInt(1,orgId);
	// preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
	// rs = preparedStatement.executeQuery();
	// while (rs.next()) {
	// payRunPeriodId = rs.getInt(1);
	// period = rs.getString(2);
	// payRunPeriodIds.add(payRunPeriodId);
	// }
	// logger.info("Fetched in getPayRunPeriodId:"+ payRunPeriodIds);
	// } catch (Exception e) {
	// logger.info("Error in getPayRunPeriodId", e);
	// throw new ApplicationException(e);
	// } finally {
	// closeResources(rs, preparedStatement, con);
	// }
	// return payRunPeriodIds;
	// }

	public List<PayRunPaymentTypeVo> getPayRunPaymentType(int orgId, Connection con) throws ApplicationException {

		List<PayRunPaymentTypeVo> payRunPaymentTypeVos = new ArrayList<>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {

			preparedStatement = con.prepareStatement(PayRollConstants.GET_PAY_RUN_PAYMENT_TYPE);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PayRunPaymentTypeVo period = new PayRunPaymentTypeVo();
				period.setId(rs.getString(1));
				period.setName(rs.getString(2));
				period.setValue(rs.getString(2));
				period.setPayRunPaymentDesc(rs.getString(3));
				period.setPayRunPaymentParentName(rs.getString(4));
				period.setPayRunPaymentIsBase(rs.getBoolean(5));
				period.setPayRunPaymentDebitOrCredit(rs.getString(6));
				payRunPaymentTypeVos.add(period);
			}
			logger.info("Fetched in getReportPeriods:" + payRunPaymentTypeVos);
		} catch (Exception e) {
			logger.info("Error in getReportPeriods", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return payRunPaymentTypeVos;
	}

	public List<PayRunPaymentCycleVo> getPayRunCycleType(int orgId, Connection con) throws ApplicationException {

		List<PayRunPaymentCycleVo> payRunPaymentCycleVos = new ArrayList<>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {

			preparedStatement = con.prepareStatement(PayRollConstants.GET_PAY_RUN_PAYMENT_CYCLE_TYPE);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PayRunPaymentCycleVo paymentCycle = new PayRunPaymentCycleVo();
				paymentCycle.setId(rs.getInt(1));
				paymentCycle.setName(rs.getString(2));
				paymentCycle.setValue(rs.getString(3));
				paymentCycle.setStartDate(rs.getString(4));
				paymentCycle.setEndDate(rs.getString(5));

				payRunPaymentCycleVos.add(paymentCycle);
			}
			logger.info("Fetched in getPayRunCycleType:" + payRunPaymentCycleVos);
		} catch (Exception e) {
			logger.info("Error in getPayRunCycleType", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return payRunPaymentCycleVos;
	}

	public List<PayRunEmployeeVo> getPayRunEmployees(int orgId, Connection con) throws ApplicationException {
		List<PayRunEmployeeVo> PayRunEmployeeVos = new ArrayList<>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {

			preparedStatement = con.prepareStatement(PayRollConstants.GET_PAY_RUN_EMPLOYEE);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PayRunEmployeeVo employeesData = new PayRunEmployeeVo();
				Integer empId = rs.getInt(1);
				employeesData.setId(empId);
				String employeeId = rs.getString(2);
				employeesData.setName(rs.getString(3));
				employeesData.setValue(empId.toString());
				employeesData.setEmail(rs.getString(4));
				employeesData.setDateOfJoining(rs.getString(5));
				employeesData.setMobileNo(rs.getString(6));
				employeesData.setDepartmentId(rs.getString(7));
				employeesData.setReportingToId(rs.getString(8));
				employeesData.setEmployeeTypeId(rs.getString(9));
				employeesData.setIsSuperAdmin(rs.getBoolean(10));
				PayRunEmployeeVos.add(employeesData);
			}
			logger.info("Fetched in getPayRunEmployees:" + PayRunEmployeeVos);
		} catch (Exception e) {
			logger.info("Error in getPayRunEmployees", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return PayRunEmployeeVos;
	}

	public PayRunVo activateDeActivatePayRun(int orgId, String organizationName, String userId, int id, String roleName,
			String status) throws ApplicationException {
		logger.info("Entry into activateDeActivatePayRun DAO - Org id " + orgId + " org name " + organizationName
				+ " user id " + userId + " rolename " + roleName + " status " + status + " payrun id " + id);
		Connection conn = getPayrollConnection();
		PreparedStatement preparedStatement = null;
		PayRunVo payRunVo = new PayRunVo();
		try {
			preparedStatement = conn.prepareStatement(PayRunConstants.DEACTIVATE_PAY_RUN);
			preparedStatement.setString(1, status);
			preparedStatement.setInt(2, orgId);
			preparedStatement.setString(3, userId);
			preparedStatement.setString(4, roleName);
			preparedStatement.setInt(5, id);
			preparedStatement.executeUpdate();
			payRunVo.setPayRunId(id);

		} catch (Exception e) {
			logger.info("Error in activateDeActivatePayRun  ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatement, conn);
		}

		return payRunVo;
	}

	public List<PayRunTableVo> getPayRunTableDropdownDAO(Integer orgId, Connection con) throws ApplicationException {
		List<PayRunTableVo> payRunTableVos = new ArrayList<>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String tableName, fullName, payType, finalName = null;
		try {

			preparedStatement = con.prepareStatement(PayRollConstants.GET_PAY_TABLE_DROPDOWN);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PayRunTableVo payRunTableVo = new PayRunTableVo();
				tableName = rs.getString(2);
				payRunTableVo.setId(rs.getInt(1));
				payRunTableVo.setName(tableName);
				payRunTableVo.setValue(rs.getString(3));
				payType = rs.getString(4);
				payRunTableVo.setPayType(payType);
				payRunTableVo.setIsShowColumn(rs.getBoolean(5));
				finalName = tableName.replaceAll("[^a-zA-Z0-9]+", "");
				fullName = payType.toLowerCase().concat(finalName);
				payRunTableVo.setFinalName(fullName);
				payRunTableVos.add(payRunTableVo);
			}
			logger.info("Fetched in getPayRunTableDropdownDAO:" + payRunTableVos);
		} catch (Exception e) {
			logger.info("Error in getPayRunTableDropdownDAO", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return payRunTableVos;
	}

	public List<PayRunTableVo> updatePayRunTableSettings(List<PayRunTableVo> payRunTableVos)
			throws ApplicationException {

		final Connection conn = getPayrollConnection();
		try {

			payRunTableVos.forEach(info -> {
				try {
					updatePayRunTableSettingsInfo(info, conn);
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		} finally {
			closeResources(null, null, conn);
		}

		return payRunTableVos;
	}

	private void updatePayRunTableSettingsInfo(PayRunTableVo info, Connection conn) throws ApplicationException {

		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = conn.prepareStatement(PayRunConstants.UPDATE_PAYRUN_TABLE_SETTINGS,
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setBoolean(1, info.getIsShowColumn());
			preparedStatement.setInt(2, info.getId());
			int rowAffected = preparedStatement.executeUpdate();
			logger.info("updatePayRunTableSettingsInfo successful " + rowAffected);
		} catch (Exception e) {
			logger.info("Error in updatePayRunTableSettingsInfo ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatement, null);
		}
	}

	public PayRunVo createImportPayRun(PayRunVo payRunVo) throws ApplicationException {
		logger.info("To create a new import Pay run" + payRunVo);
		Connection connection = null;
		if (payRunVo.getOrgId() != null && payRunVo.getUserId() != null) {
			try {
				connection = getPayrollConnection();
				connection.setAutoCommit(false);
				@SuppressWarnings("unused")
				ObjectMapper mapper = new ObjectMapper();
				@SuppressWarnings("unchecked")			
				List<PayrollUploadVo> payrunListOne = (List<PayrollUploadVo>) payRunVo.getPayRunInformation();

				logger.info("payrunListOne "+payrunListOne);
				
	


				Map<String, List<PayrollUploadVo>> hashMap = new HashMap<>();
//				logger.info("payPeriodList "+payPeriodList);

				for (PayrollUploadVo data : payrunListOne) {
					String key = data.getPayPeriod();
					if (!hashMap.containsKey(key)) {
						List<PayrollUploadVo> list = new ArrayList<>();
						list.add(data);

						hashMap.put(key, list);
					} else {
						hashMap.get(key).add(data);
					}
				}

				logger.info("hashMap " + hashMap);

				for (Entry<String, List<PayrollUploadVo>> entry : hashMap.entrySet()) {
					logger.info("key : " + entry.getKey());
					logger.info("value " + entry.getValue());
					payRunVo.setPayPeriod(entry.getKey());
					payRunVo.setPayRunInformation(entry.getValue());
					createPayRunImportData(payRunVo, connection);
				}

				if (payRunVo.getAttachments() != null) {
					attachmentsDao.createAttachments(payRunVo.getOrgId(), payRunVo.getUserId(),
							payRunVo.getAttachments(), AttachmentsConstants.MODULE_TYPE_PAYROLL_PAYRUN,
							payRunVo.getPayRunId(), payRunVo.getRoleName());
				}

				connection.commit();
				logger.info("Succecssfully created Pay run in PayRunDao" + payRunVo);
			} catch (Exception e) {
				logger.info("Error in Pay Run creation :: ", e);
				try {
					connection.rollback();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					throw new ApplicationException(e1.getMessage());
				}
				logger.info("Before throw in dao :: " + e.getMessage());
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(null, null, connection);
			}
		} else {
			throw new ApplicationException("No Data for PayRun Organization and User Id");
		}
		return payRunVo;
	}

	@SuppressWarnings("unchecked")
	private PayRunVo createPayRunImportData(PayRunVo payRunVo, Connection connection)
			throws ApplicationException, IOException {
		PreparedStatement preparedStatementrun = null;
		ObjectMapper mapper = new ObjectMapper();

		String payRunReference = getPayRunReferenceforImport(payRunVo, connection);
		logger.info(" createPayRunImportData - payRunReference " + payRunReference);
		String payrunDate = LocalDate.now().toString();

		payRunVo.setPayrunReference(payRunReference);

		payRunVo.setPayrunDate(payrunDate);

		if (!payRunVo.getPayRunRefPrefix().isEmpty() && !payRunVo.getPayRunRefSuffix().isEmpty()) {
			StringBuilder payRunReferenceBuilder = new StringBuilder();
			payRunReferenceBuilder
					.append(payRunVo.getPayRunRefPrefix() != null && !payRunVo.getPayRunRefPrefix().isEmpty()
							? payRunVo.getPayRunRefPrefix()
							: " ")
					.append("~").append(payRunVo.getPayrunReference()).append("~")
					.append(payRunVo.getPayRunRefSuffix() != null && !payRunVo.getPayRunRefSuffix().isEmpty()
							? payRunVo.getPayRunRefSuffix()
							: " ");
			payRunReference = payRunReferenceBuilder.toString();
		}
		if (payRunVo.getPayRunRefPrefix() != null && payRunVo.getPayRunRefSuffix().isEmpty()) {
			StringBuilder payRunReferenceBuilder = new StringBuilder();
			payRunReferenceBuilder
					.append(payRunVo.getPayRunRefPrefix() != null && !payRunVo.getPayRunRefPrefix().isEmpty()
							? payRunVo.getPayRunRefPrefix()
							: " ")
					.append("~").append(payRunVo.getPayrunReference());
			payRunReference = payRunReferenceBuilder.toString();
		}
		if (payRunVo.getPayRunRefPrefix().isEmpty() && payRunVo.getPayRunRefSuffix() != null) {
			StringBuilder payRunReferenceBuilder = new StringBuilder();
			payRunReferenceBuilder.append(payRunVo.getPayrunReference()).append("~")
					.append(payRunVo.getPayRunRefSuffix() != null && !payRunVo.getPayRunRefSuffix().isEmpty()
							? payRunVo.getPayRunRefSuffix()
							: " ");
			payRunReference = payRunReferenceBuilder.toString();
		}

		if (payRunVo.getPayRunRefPrefix().isEmpty() && payRunVo.getPayRunRefSuffix().isEmpty()) {
			payRunReference = payRunVo.getPayrunReference();
		}

		boolean isPayRunReferenceExist = checkPayRunReferenceExistForAnOrganization(payRunVo.getOrgId(), connection,
				payRunReference);
		if (isPayRunReferenceExist) {
			throw new ApplicationException("PayRun reference already exists");
		}

		try {
			preparedStatementrun = connection.prepareStatement(PayRunConstants.INSERT_INTO_PAY_RUN,
					Statement.RETURN_GENERATED_KEYS);
			preparedStatementrun.setString(1, payRunReference != null ? payRunReference : null);
			preparedStatementrun.setString(2, payRunVo.getPayrunDate() != null ? payRunVo.getPayrunDate() : null);
			preparedStatementrun.setString(3, payRunVo.getPayPeriod() != null ? payRunVo.getPayPeriod() : null);
			preparedStatementrun.setString(4, payRunVo.getStatus() != null ? payRunVo.getStatus() : null);
			preparedStatementrun.setString(5, payRunVo.getUserId() != null ? payRunVo.getUserId() : null);
			preparedStatementrun.setLong(6, payRunVo.getOrgId() != null ? payRunVo.getOrgId() : 0);
			preparedStatementrun.setString(7, payRunVo.getRoleName() != null ? payRunVo.getRoleName() : null);
			preparedStatementrun.setString(8,
					payRunVo.getCopyPreviousPayRun() != null ? payRunVo.getCopyPreviousPayRun() : null);
			preparedStatementrun.setString(9, payRunVo.getPaymentCycle() != null ? payRunVo.getPaymentCycle() : null);

			String glData = payRunVo.getGeneralLedgerData() != null
					? mapper.writeValueAsString(payRunVo.getGeneralLedgerData())
					: null;
			logger.info("glData>> " + glData);
			preparedStatementrun.setString(10, glData);
			String jsn = mapper.writeValueAsString(payRunVo.getPayRunInformation());
			logger.info("Insert info " + jsn);
			List<Map<String, Object>> payrunMapList = mapper.readValue(jsn, ArrayList.class);
			String employeeId = null;
			Double earnings = 0.0;
			Double deductions = 0.0;
			List<String> employeeList = new ArrayList<>();

			for (Map<String, Object> map : payrunMapList) {

				for (Map.Entry<String, Object> entry : map.entrySet()) {

					if (entry.getKey().contains("employeeId")) {
						employeeId = String.valueOf(entry.getValue());
						employeeList.add(employeeId);
						logger.info("pay run object employee--- " + employeeId);
					}

					if (employeeList.contains(employeeId)) {
						if (entry.getKey().contains("earnings")) {

							if (entry.getValue() == null) {
								earnings += 0;
							} else {
								earnings += Double.parseDouble((String) entry.getValue());
							}
							logger.info("earnings--" + entry.getKey() + "::" + entry.getValue());

						} else if (entry.getKey().contains("deductions")) {
							if (entry.getValue() == null) {
								deductions += 0;
							} else {
								deductions += Double.parseDouble((String) entry.getValue());
							}
							logger.info("deductions--" + entry.getKey() + "::" + entry.getValue());

						}
						if (entry.getKey().contains("totalEarnings")) {

							entry.setValue(earnings);
							logger.info("Total - earnings " + entry.getValue());
						}
						if (entry.getKey().contains("totalDeductions")) {

							entry.setValue(deductions);
							logger.info("Total - Deductions " + entry.getValue());
						}
						if (entry.getKey().contains("netPayable")) {
							Double netPayable = earnings - deductions;
							entry.setValue(netPayable);
							logger.info("Net Payable " + entry.getValue());
						}

					}

				}
				logger.info("Total earnings -- " + earnings + "Total deductions -- " + deductions + "---for employee---"
						+ employeeId);
			}
			logger.info("final map " + payrunMapList);
			String jsn2 = mapper.writeValueAsString(payrunMapList);
			logger.info("final string " + jsn2);
			preparedStatementrun.setString(11, jsn2);
			preparedStatementrun.setString(12, null);
			preparedStatementrun.setInt(13, 286);

			int rowAffected = preparedStatementrun.executeUpdate();
			if (rowAffected == 1) {
				try (ResultSet rs = preparedStatementrun.getGeneratedKeys()) {
					while (rs.next()) {
						payRunVo.setPayRunId(rs.getInt(1));
					}
				}
			}

		} catch (Exception e) {
			logger.info("Error in createPayRunData ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatementrun, null);
		}

		return payRunVo;

	}

	private String getPayRunReferenceforImport(PayRunVo payRunVo, Connection conn) throws ApplicationException {
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String payRunRefNumber = null;
		String[] payrunrefTemp = null;
		Random randomPayRunNumber = new Random();
		int n = randomPayRunNumber.nextInt(100000);
		String formatted = String.format("%04d", n);

		try {
			preparedStatement = conn.prepareStatement(PayRunConstants.GET_PAYRUN_REFERENCE_IMPORT,
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, payRunVo.getOrgId());
			preparedStatement.setInt(2, payRunVo.getOrgId());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String str = rs.getString(1);
				logger.info("str " + str);
				if (str.contains("~")) {
					payrunrefTemp = rs.getString(1).split("~");
					logger.info("1::" + payrunrefTemp[0] + "2::" + payrunrefTemp[1]);

					if (payrunrefTemp.length > 1) {
						Integer num = Integer.parseInt(payrunrefTemp[1]) + 1;

						payRunRefNumber = num.toString();

					} else {
						payRunRefNumber = formatted;

					}
				}

			}

			if (payRunRefNumber == null) {
				payRunRefNumber = formatted;
			}
			logger.info("Fetched in getPayRunReferenceforImport:" + payRunRefNumber);
		} catch (Exception e) {
			logger.info("Error in getPayRunReferenceforImport", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return payRunRefNumber;

	}

	public List<PayRunVo> getACtiveAllPayRun(Integer orgId, String userId, String roleName)
			throws ApplicationException {
		logger.info("To get Pay run data for the org " + orgId + " and userid " + userId + " and roleName " + roleName);
		List<PayRunVo> payRunVos = new ArrayList<PayRunVo>();

		if (orgId != null && userId != null && roleName != null) {
			payRunVos = getAllActivePayRunData(orgId, userId, roleName);
			payRunVos.forEach(info -> {
				List<UploadFileVo> uploadFileVos = new ArrayList<UploadFileVo>();

				try {
					for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(info.getPayRunId(),
							AttachmentsConstants.MODULE_TYPE_PAYROLL_PAYRUN)) {

						UploadFileVo uploadFileVo = new UploadFileVo();
						uploadFileVo.setId(attachments.getId());
						// uploadFileVo.setData(attachments.getD);
						uploadFileVo.setName(attachments.getFileName());
						uploadFileVo.setSize(attachments.getSize());
						uploadFileVos.add(uploadFileVo);
						logger.info("get Payrun attachment " + uploadFileVo);
					}
				} catch (ApplicationException e) {

					throw new ApplicationRuntimeException(e);
				}
				info.setAttachments(uploadFileVos);
			});
		}
		/*
		 * else { throw new
		 * ApplicationException("Org Id , UserId , RoleName and PayRunId are null"); }
		 */

		return payRunVos;

	}

	private List<PayRunVo> getAllActivePayRunData(Integer orgId, String userId, String roleName)
			throws ApplicationException {
		logger.info("To get getAllActivePayRunData for the org " + orgId + " and userid " + userId + " and roleName "
				+ roleName);

		List<PayRunVo> payRunVos = new ArrayList<PayRunVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = getAccountsPayable();

			if (roleName.equals("Super Admin")) {
				preparedStatement = conn.prepareStatement(PayRunConstants.SELECT_PAY_RUN_ALL_ORGANIZATION_ACT,
						Statement.RETURN_GENERATED_KEYS);
			} else {
				preparedStatement = conn.prepareStatement(PayRunConstants.SELECT_PAY_RUN_USER_ROLE_ACT,
						Statement.RETURN_GENERATED_KEYS);
			}

			preparedStatement.setInt(1, orgId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);

			if (!(roleName.equals("Super Admin"))) {
				preparedStatement.setString(3, userId);
				preparedStatement.setString(4, roleName);
				preparedStatement.setString(5, CommonConstants.STATUS_AS_ACTIVE);

			}

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PayRunVo payRunVo = new PayRunVo();
				Integer payRunId = rs.getInt(1);
				payRunVo.setPayRunId(payRunId);
				String payrefNum = rs.getString(2) != null ? rs.getString(2).replace("~", "/") : null;
				payRunVo.setPayrunReference(payrefNum);
				payRunVo.setPayrunDate(rs.getString(3));
				payRunVo.setPayPeriod(rs.getString(4));
				payRunVo.setStatus(getDisplayStatus(rs.getString(5)));
				payRunVo.setUserId(rs.getString(6));
				payRunVo.setOrgId(rs.getInt(7));
				payRunVo.setRoleName(rs.getString(8));
				payRunVo.setCopyPreviousPayRun(rs.getString(9));
				payRunVo.setPayRunPaidAmount(null);
				payRunVo.setPaymentCycle(rs.getString(10));
				String json2 = rs.getString(11);
				ObjectMapper mapper = new ObjectMapper();
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> payrunMapList = mapper.readValue(json2, ArrayList.class);
				String employeeId = null;
				Double payRunAmount = 0.0;
				List<String> employeeList = new ArrayList<>();

				for (Map<String, Object> map : payrunMapList) {

					for (Map.Entry<String, Object> entry : map.entrySet()) {

						if (entry.getKey().contains("employeeId")) {
							employeeId = String.valueOf(entry.getValue());
							employeeList.add(employeeId);
							logger.info("pay run object employee--- " + employeeId);
						}

						if (employeeList.contains(employeeId)) {
							if (entry.getKey().contains("netPayable")) {
								logger.info("netPayable --" + entry.getKey() + "::" + entry.getValue());
								payRunAmount += Double.parseDouble(entry.getValue().toString());

							}

						}

					}

				}
				logger.info("payRunAmount -all " + payRunAmount);
				logger.info("Employee count - all :" + employeeList.size());
				payRunVo.setEmployeeCount(employeeList.size());
//				payRunVo.setPayRunAmount(BigDecimal.valueOf(payRunAmount));
				if (json2 != null) {
					// ObjectMapper mapper = new ObjectMapper();
					JsonNode readTree = mapper.readTree(json2);
					logger.info("json node " + readTree);
					payRunVo.setPayRunInformation(readTree);
				}
				CurrencyVo currencyVo = currencyDao.getCurrency(rs.getInt(12));
				payRunVo.setCurrencyId(rs.getInt(12));

				 if(payRunAmount != null && currencyVo != null ) {
					String finalPayRunAmount = (currencyVo.getSymbol() != null ? currencyVo.getSymbol() : "") + " "+BigDecimal.valueOf(payRunAmount).setScale(2, RoundingMode.CEILING).toString();

					payRunVo.setPayRunAmount(finalPayRunAmount);

				 } else {
					 payRunVo.setPayRunAmount(BigDecimal.valueOf(payRunAmount).setScale(2, RoundingMode.CEILING).toString());
				 }


				payRunVos.add(payRunVo);
				logger.info("getPayRunDataAll " + payRunVo);
			}

			logger.info("getPayRunDataAll2 " + payRunVos);
		} catch (Exception e) {
			logger.info("Error in getPayRunDataAll ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, conn);
		}
		return payRunVos;
	}

	public List<PayRunEmployeeAmountVo> getPayRunDataByOrganizationEmployeeId(int organizationId, int employeeId)
			throws ApplicationException {

		logger.info("Entry into method getPayRunDataByOrganizationEmployeeId");
		Connection con = null;
		List<PayRunEmployeeAmountVo> payRunVos = new ArrayList<PayRunEmployeeAmountVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getPayrollConnection();
			preparedStatement = con.prepareStatement(PayRunConstants.SELECT_PAY_RUN_ORG_EMP_ID);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
//			preparedStatement.setString(3, CommonConstants.STATUS_AS_DRAFT);
			preparedStatement.setString(3, CommonConstants.STATUS_AS_PAID);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_PARTIALLY_PAID);
			preparedStatement.setInt(5, employeeId);

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PayRunEmployeeAmountVo payRunVo = new PayRunEmployeeAmountVo();
				payRunVo.setId(rs.getInt(1));
				payRunVo.setValue(rs.getInt(1));
				String payrunRef = rs.getString(2);
				if (payrunRef != null)
					payrunRef = payrunRef.replace("~", "/");
				payRunVo.setName(payrunRef);
				payRunVo.setAmount(rs.getDouble(3));
				payRunVo.setBalanceDue(rs.getDouble(3));
				payRunVo.setEmployeeId(employeeId);
				payRunVos.add(payRunVo);
			}
		} catch (Exception e) {
			logger.info("Error in getPayRunDataByOrganizationEmployeeId - info ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return payRunVos;

	}

	private void createPayRunEmployee( List<PayRunEmployeePaymentVo> listEmployee) throws ApplicationException {
		logger.info("Entry into method: createPayRunEmployee");
		PreparedStatement preparedStatement = null;
		Connection con = null;
		try {
			con = getPayrollConnection();
			String sql = PayRunConstants.DELETE_PAYRUN_EMPLOYEE;
			String sql1 = PayRunConstants.INSERT_PAYRUN_EMPLOYEE;
			for(int i=0;i<listEmployee.size();i++){
				PayRunEmployeePaymentVo payRunEmployeePaymentVo =listEmployee.get(i);
				preparedStatement = con.prepareStatement(sql);
				preparedStatement.setString(1,payRunEmployeePaymentVo.getPayrunReference());
				preparedStatement.executeUpdate();
				closeResources(null, preparedStatement, null);
			}
			for(int i=0;i<listEmployee.size();i++){
				PayRunEmployeePaymentVo payRunEmployeePaymentVo =listEmployee.get(i);
				preparedStatement = con.prepareStatement(sql1);
				preparedStatement.setString(1, payRunEmployeePaymentVo.getPayrunReference());
				preparedStatement.setString(2, payRunEmployeePaymentVo.getEmpId());
				preparedStatement.setDouble(3, payRunEmployeePaymentVo.getAmount());
				preparedStatement.setDouble(4, payRunEmployeePaymentVo.getAmount());
				preparedStatement.executeUpdate();
			}

		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatement, con);
		}
	}

	public List<PayRunEmployeeAmountVo> getEmployeesByPayrunId(int organizationId, int payrunId) throws ApplicationException {

		logger.info("Entry into method getEmployeesByPayrunId");
		Connection con = null;
		List<PayRunEmployeeAmountVo> payRunVos = new ArrayList<PayRunEmployeeAmountVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getPayrollConnection();
			preparedStatement = con.prepareStatement(PayRunConstants.SELECT_EMPLOYEES_BY_PAYRUN_ID);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(3, CommonConstants.STATUS_AS_PAID);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_PARTIALLY_PAID);
			preparedStatement.setInt(5, payrunId);

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PayRunEmployeeAmountVo payRunVo = new PayRunEmployeeAmountVo();
				payRunVo.setId(payrunId);
				payRunVo.setValue(payrunId);
				payRunVo.setName(null);
				payRunVo.setAmount(rs.getDouble(2));
				payRunVo.setEmployeeId(rs.getInt(1));
				payRunVos.add(payRunVo);
			}
		} catch (Exception e) {
			logger.info("Error in getPayRunDataByOrganizationEmployeeId - info ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return payRunVos;

	}

	
	public String  getPayRunReferencNumber(Integer payrunId) throws ApplicationException {

		logger.info("Entry into method getPayRunReferencNumber");
		Connection con = null;
		String referenceNum = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getPayrollConnection();
			preparedStatement = con.prepareStatement(PayRunConstants.GET_PAY_RUN_REF_BY_ID);
			preparedStatement.setInt(1, payrunId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				referenceNum = rs.getString(1).replace("~", "/");
			}
		} catch (Exception e) {
			logger.info("Error in getPayRunReferencNumber - info ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return referenceNum;

	}


	public PayrollDueVo getPayrollPayable(int organizationId, String userId, String roleName) throws ApplicationException {

		PreparedStatement ps = null;
		ResultSet rs = null;
		PayrollDueVo payrollPayable = new PayrollDueVo();
		List<PayrollDueItemVo> payrolls = new ArrayList<>();
		Double totalAmount = 0.0;
		Double overdueAmount = 0.0;
		String symbol = null;
		int count = 0;
		Connection con =null;
		try {
			con = getPayrollConnection();
			if (!roleName.equalsIgnoreCase(CommonConstants.ROLE_SUPER_ADMIN)) {
				ps = con.prepareStatement(PayRunConstants.GET_PAYROLL_PAYABLE_USER_ROLE);
				ps.setInt(1, organizationId);
				ps.setString(2, CommonConstants.STATUS_AS_PAID);
				ps.setString(3, CommonConstants.STATUS_AS_DRAFT);
				ps.setString(4, CommonConstants.STATUS_AS_VOID);
				ps.setString(5, CommonConstants.STATUS_AS_DELETE);
				ps.setString(6, userId);
				ps.setString(7, roleName);
			} else {
				ps = con.prepareStatement(PayRunConstants.GET_PAYROLL_PAYABLE);
				ps.setInt(1, organizationId);
				ps.setString(2, CommonConstants.STATUS_AS_PAID);
				ps.setString(3, CommonConstants.STATUS_AS_DRAFT);
				ps.setString(4, CommonConstants.STATUS_AS_VOID);
				ps.setString(5, CommonConstants.STATUS_AS_DELETE);
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				PayrollDueItemVo payroll = new PayrollDueItemVo();
				payroll.setId(rs.getInt(1));
				payroll.setPayPeriod(rs.getString(2));
				String payRunRef = rs.getString(3);
				if (payRunRef != null)
					payRunRef = payRunRef.replace("~", "/");
				payroll.setPayRunReference(payRunRef);
				String amount = rs.getString(4);
				symbol = rs.getString(7);
				java.util.Date payrunDate = DateConverter.getInstance().convertStringToTimestamp(rs.getString(8));
				if (symbol != null && amount != null) {
					payroll.setPayRunAmount(symbol + " " + amount);
					String dueBalance = rs.getString(5) != null ? rs.getString(5) : amount;
					payroll.setPaidAmount(symbol + " " + String.valueOf(Double.parseDouble(amount) - Double.parseDouble(dueBalance)));
					totalAmount += Double.parseDouble(dueBalance);
					String status = rs.getString(6);
					if (payrunDate.before(new Date(System.currentTimeMillis()))) {
						overdueAmount += Double.parseDouble(dueBalance);
					}
					switch(status) {
					case CommonConstants.STATUS_AS_ACTIVE:
						status = CommonConstants.DISPLAY_STATUS_AS_OPEN;
						break;
					case CommonConstants.STATUS_AS_OVERDUE:
						status = CommonConstants.DISPLAY_STATUS_AS_OVERDUE;
						break;
					case CommonConstants.STATUS_AS_PARTIALLY_PAID:
						status = CommonConstants.DISPLAY_STATUS_AS_PARTIALLY_PAID;
						break;
					case CommonConstants.STATUS_AS_OPEN:
						status = CommonConstants.DISPLAY_STATUS_AS_OPEN;
						break;
					}
					payroll.setStatus(status);
				}
				payroll.setPayrunDate(DateConverter.getInstance().convertDateToGivenFormat(payrunDate, "yyyy-MM-dd"));
				if (count < 10)
					payrolls.add(payroll);	
				count++;
			}
			payrollPayable.setPayrolls(payrolls);
			DecimalFormat df = new DecimalFormat("#.##");
			df.setRoundingMode(RoundingMode.CEILING);
			payrollPayable.setTotalAmount(symbol + " " + String.valueOf(df.format(totalAmount)));
			payrollPayable.setOverdueAmount(symbol + " " + String.valueOf(df.format(overdueAmount)));
		} catch (Exception e) {
			throw new ApplicationException(e);

		} finally {
			closeResources(rs, ps, con);
		}
		return payrollPayable;
	
	}
	
	
}
