package com.blackstrawai.ar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ar.applycredits.ApplyCreditDetailsVo;
import com.blackstrawai.ar.applycredits.ApplyCreditsBasicVo;
import com.blackstrawai.ar.applycredits.ApplyCreditsInvoiceVo;
import com.blackstrawai.ar.applycredits.ApplyCreditsVo;
import com.blackstrawai.ar.receipt.ReceiptSettingsVo;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.attachments.AttachmentsDao;
import com.blackstrawai.attachments.AttachmentsVo;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.keycontact.CustomerDao;
import com.blackstrawai.keycontact.customer.CustomerVo;
import com.blackstrawai.settings.CurrencyDao;
import com.blackstrawai.settings.CurrencyVo;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class ApplyCreditsDao extends BaseDao {

	private Logger logger = Logger.getLogger(ApplyCreditsDao.class);

	@Autowired
	AttachmentsDao attachmentsDao;

	@Autowired
	CustomerDao customerDao;

	@Autowired
	private CurrencyDao currencyDao;

	public Boolean createApplyCredits(ApplyCreditsVo applyCreditsVo) throws ApplicationException {
		logger.info("Entry into method: createApplyCredits");
		boolean isCreatedSuccessfully = false;
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		if (applyCreditsVo != null) {
			try {

				con = getAccountsReceivableConnection();
				con.setAutoCommit(false);
				boolean isApplyCreditExist=checkApplyCreditExist(applyCreditsVo, con);
				if(isApplyCreditExist){
					throw new Exception("ApplyCredit Voucher No. Exist for the Organization");
				}
				String sql = ApplyCreditsConstants.INSERT_INTO_APPLY_CREDITS;
				preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setString(1,
						applyCreditsVo.getVoucherNo() != null ? applyCreditsVo.getVoucherNo() : null);
				preparedStatement.setObject(2, applyCreditsVo.getDate() != null ? applyCreditsVo.getDate() : null);
				preparedStatement.setObject(3,
						applyCreditsVo.getCustomerId() != null ? applyCreditsVo.getCustomerId() : null);
				preparedStatement.setObject(4,
						applyCreditsVo.getLedgerId() != null ? applyCreditsVo.getLedgerId() : null);
				preparedStatement.setString(5,
						applyCreditsVo.getLedgerName() != null ? applyCreditsVo.getLedgerName() : null);
				preparedStatement.setObject(6,
						applyCreditsVo.getCurrencyId() != null ? applyCreditsVo.getCurrencyId() : null);
				preparedStatement.setString(7,
						applyCreditsVo.getInvoiceTotalAmount() != null ? applyCreditsVo.getInvoiceTotalAmount() : null);
				preparedStatement.setString(8,
						applyCreditsVo.getAdjustedCreditAmount() != null ? applyCreditsVo.getAdjustedCreditAmount()
								: null);
				preparedStatement.setString(9,
						applyCreditsVo.getAvailableFund() != null ? applyCreditsVo.getAvailableFund() : null);
				preparedStatement.setObject(10,
						applyCreditsVo.getOrganizationId() != null ? applyCreditsVo.getOrganizationId() : null);
				preparedStatement.setString(11, applyCreditsVo.getStatus() != null ? applyCreditsVo.getStatus() : null);
				preparedStatement.setObject(12,
						applyCreditsVo.getIsSuperAdmin() != null ? applyCreditsVo.getIsSuperAdmin() : null);
				preparedStatement.setObject(13,
						applyCreditsVo.getUserId() != null ? Integer.valueOf(applyCreditsVo.getUserId()) : null);
				List<ReceiptSettingsVo> customTableList = applyCreditsVo.getCustomTableList();
				ObjectMapper mapper = new ObjectMapper();
				String newJsonData = mapper.writeValueAsString(customTableList);
				preparedStatement.setString(14, newJsonData != null ? newJsonData : null);
				preparedStatement.setString(15,
						applyCreditsVo.getRoleName() != null ? applyCreditsVo.getRoleName() : null);
				preparedStatement.setString(16,
						applyCreditsVo.getExchangeRate() != null ? applyCreditsVo.getExchangeRate() : null);
				int rowAffected = preparedStatement.executeUpdate();
				if (rowAffected == 1) {
					rs = preparedStatement.getGeneratedKeys();
					if (rs.next()) {
						applyCreditsVo.setId(rs.getInt(1));
					}
				}
				if (applyCreditsVo.getCreditDetails() != null && applyCreditsVo.getCreditDetails().size() > 0) {
					for (ApplyCreditDetailsVo applyCreditDetailsVo : applyCreditsVo.getCreditDetails()) {
						applyCreditDetailsVo.setStatus(CommonConstants.STATUS_AS_ACTIVE);
						createCreditDetails(applyCreditDetailsVo, applyCreditsVo.getId(), con);
					}
				}
				if (applyCreditsVo.getInvoiceDetails() != null && applyCreditsVo.getInvoiceDetails().size() > 0) {
					for (ApplyCreditsInvoiceVo applyCreditsInvoiceVo : applyCreditsVo.getInvoiceDetails()) {
						applyCreditsInvoiceVo.setStatus(CommonConstants.STATUS_AS_ACTIVE);
						createInvoiceDetails(applyCreditsInvoiceVo, applyCreditsVo.getId(), con);
					}

				}

				if (applyCreditsVo.getAttachments() != null && applyCreditsVo.getAttachments().size() > 0) {
					attachmentsDao.createAttachments(applyCreditsVo.getOrganizationId(), applyCreditsVo.getUserId(),
							applyCreditsVo.getAttachments(),
							AttachmentsConstants.MODULE_TYPE_VENDOR_PORTAL_BALANCE_CONFIRMATION, applyCreditsVo.getId(),
							applyCreditsVo.getRoleName());
				}

				con.commit();
				isCreatedSuccessfully = true;
			} catch (Exception e) {
				try {
					con.rollback();
				} catch (SQLException e1) {
					throw new ApplicationException(e1.getMessage());
				}
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(rs, preparedStatement, con);
			}
		}
		return isCreatedSuccessfully;
	}

	private void createInvoiceDetails(ApplyCreditsInvoiceVo applyCreditsInvoiceVo, Integer id, Connection con)
			throws ApplicationException {
		logger.info("Entry into method: createInvoiceDetails");
		PreparedStatement preparedStatement =null;
		try {
			preparedStatement = con.prepareStatement(ApplyCreditsConstants.INSERT_INTO_APPLY_CREDITS_INVOICE_DETAILS);
			preparedStatement.setObject(1,
					applyCreditsInvoiceVo.getInvoiceId() != null ? applyCreditsInvoiceVo.getInvoiceId() : null);
			preparedStatement.setString(2,
					applyCreditsInvoiceVo.getInvoiceAmount() != null ? applyCreditsInvoiceVo.getInvoiceAmount() : null);
			preparedStatement.setString(3,
					applyCreditsInvoiceVo.getAppliedAmount() != null ? applyCreditsInvoiceVo.getAppliedAmount() : null);
			preparedStatement.setObject(4, id != null ? id : null);
			preparedStatement.setString(5,
					applyCreditsInvoiceVo.getStatus() != null ? applyCreditsInvoiceVo.getStatus() : null);
			String data = "{\"data\":";
			data += "{\n" + "\"bankCharges\":\"" + applyCreditsInvoiceVo.getBankCharges() + "\"," + "\"tdsDeducted\":\""
					+ applyCreditsInvoiceVo.getTdsDeducted() + "\"," + "\"others3\":\""
					+ applyCreditsInvoiceVo.getOthers3() + "\"," + "\"others1\":\"" + applyCreditsInvoiceVo.getOthers1()
					+ "\"," + "\"others2\":\"" + applyCreditsInvoiceVo.getOthers2() + "\"" + "}" + "}";
			logger.info(data);
			preparedStatement.setString(6, data);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("Error in createInvoiceDetails::", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, null);
		}
	}

	private void createCreditDetails(ApplyCreditDetailsVo applyCreditDetailsVo, Integer id, Connection con)
			throws ApplicationException {
		logger.info("Entry into method: createCreditDetails");
		PreparedStatement preparedStatement =null;
		try{
			preparedStatement = con.prepareStatement(ApplyCreditsConstants.INSERT_INTO_APPLY_CREDITS_CREDIT_DETAILS);
			preparedStatement.setObject(1,
					applyCreditDetailsVo.getReferenceId() != null ? applyCreditDetailsVo.getReferenceId() : null);
			preparedStatement.setString(2,
					applyCreditDetailsVo.getCreditType() != null ? applyCreditDetailsVo.getCreditType() : null);
			preparedStatement.setString(3,
					applyCreditDetailsVo.getOriginalAmount() != null ? applyCreditDetailsVo.getOriginalAmount() : null);
			preparedStatement.setString(4,
					applyCreditDetailsVo.getAvailableAmount() != null ? applyCreditDetailsVo.getAvailableAmount()
							: null);
			preparedStatement.setString(5,
					applyCreditDetailsVo.getAdjustmentAmount() != null ? applyCreditDetailsVo.getAdjustmentAmount()
							: null);
			preparedStatement.setObject(6, id != null ? id : null);
			preparedStatement.setString(7,
					applyCreditDetailsVo.getStatus() != null ? applyCreditDetailsVo.getStatus() : null);
			preparedStatement.setObject(8,
					applyCreditDetailsVo.getLedgerId() != null ? applyCreditDetailsVo.getLedgerId() : null);
			preparedStatement.setString(9,
					applyCreditDetailsVo.getLedgerName() != null ? applyCreditDetailsVo.getLedgerName() : null);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("Error in createCreditDetails::", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, null);
		}
	}

	public List<ApplyCreditsBasicVo> getAllApplyCreditsOfAnOrganization(int organizationId, String userId,
			String roleName) throws ApplicationException {
		logger.info("Entry into method: getAllApplyCreditsOfAnOrganization");
		List<ApplyCreditsBasicVo> listApplyCedits = new ArrayList<ApplyCreditsBasicVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsReceivableConnection();
			if (roleName.equals("Super Admin")) {
				preparedStatement = con.prepareStatement(ApplyCreditsConstants.GET_ALL_APPLY_CREDITS_ORGANIZATION);
			} else {
				preparedStatement = con.prepareStatement(ApplyCreditsConstants.GET_ALL_APPLY_CREDITS_USER_ROLE);
			}

			preparedStatement.setInt(1, organizationId);
			if (!(roleName.equals("Super Admin"))) {
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
			}
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ApplyCreditsBasicVo applyCreditsBasicVo = new ApplyCreditsBasicVo();
				applyCreditsBasicVo.setId(rs.getInt(1));
				applyCreditsBasicVo.setVoucherNo(rs.getString(2));
				Integer customerId = rs.getInt(3);
				applyCreditsBasicVo.setCustomerId(customerId);
				if (customerId != null) {
					CustomerVo customerVo = customerDao.getCustomerPrimaryInfo(customerId, con);
					if (customerVo.getPrimaryInfo().getCompanyName() != null) {
						applyCreditsBasicVo.setCustomerName(customerVo.getPrimaryInfo().getCompanyName());
					}
				}
				applyCreditsBasicVo.setDate(rs.getDate(4));
				String amount=rs.getString(5);
				CurrencyVo currencyVo = currencyDao.getCurrency(rs.getInt(6));
				if (amount != null && amount.length() > 0 && currencyVo!=null ) {// Set symbol
					amount=currencyVo.getSymbol() != null ? currencyVo.getSymbol() + " " + amount : amount;
				}
				applyCreditsBasicVo.setAmount(amount);
				applyCreditsBasicVo.setStatus(rs.getString(7));
				listApplyCedits.add(applyCreditsBasicVo);
			}

		} catch (Exception e) {
			logger.info("Error in getAllApplyCreditsOfAnOrganization::", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listApplyCedits;
	}

	public ApplyCreditsVo getApplyCreditsById(int id) throws ApplicationException {
		logger.info("Entry into method: getApplyCreditsById");
		ApplyCreditsVo applyCreditsVo = new ApplyCreditsVo();
		Connection con =null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try  {
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(ApplyCreditsConstants.GET_APPLY_CREDIT_BY_ID);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				applyCreditsVo.setId(rs.getInt(1));
				applyCreditsVo.setVoucherNo(rs.getString(2));
				applyCreditsVo.setDate(rs.getDate(3));
				applyCreditsVo.setCustomerId(rs.getInt(4));
				applyCreditsVo.setLedgerId(rs.getInt(5));
				applyCreditsVo.setLedgerName(rs.getString(6));
				applyCreditsVo.setCurrencyId(rs.getInt(7));
				String customTableList = rs.getString(8);
				List<ReceiptSettingsVo> customTable = new ArrayList<ReceiptSettingsVo>();
				if (customTableList != null && customTableList.length() > 4) {
					JSONParser parser = new JSONParser();
					JSONArray columnsArray = (JSONArray) parser.parse(customTableList);
					for (Object obj : columnsArray) {
						JSONObject columnsObj = (JSONObject) obj;
						ReceiptSettingsVo categoryReceiptCustomTableVo = new ReceiptSettingsVo();
						categoryReceiptCustomTableVo.setLedgerName(
								columnsObj.get("ledgerName") != null ? columnsObj.get("ledgerName").toString()
										: null);
						categoryReceiptCustomTableVo.setLedgerId(
								columnsObj.get("ledgerId") != null ? columnsObj.get("ledgerId").toString() : null);
						categoryReceiptCustomTableVo.setcName(
								columnsObj.get("cName") != null ? columnsObj.get("cName").toString() : null);
						categoryReceiptCustomTableVo.setColumnShow(
								columnsObj.get("columnShow") != null ? (boolean) columnsObj.get("columnShow")
										: false);
						customTable.add(categoryReceiptCustomTableVo);
					}
					applyCreditsVo.setCustomTableList(customTable);
				}
				applyCreditsVo.setInvoiceTotalAmount(rs.getString(9));
				applyCreditsVo.setAdjustedCreditAmount(rs.getString(10));
				applyCreditsVo.setAvailableFund(rs.getString(11));
				applyCreditsVo.setOrganizationId(rs.getInt(12));
				applyCreditsVo.setStatus(rs.getString(13));
				applyCreditsVo.setIsSuperAdmin(rs.getBoolean(14));
				applyCreditsVo.setUserId(rs.getString(15));
				applyCreditsVo.setRoleName(rs.getString(16));
				applyCreditsVo.setExchangeRate(rs.getString(17));
				applyCreditsVo.setCreateTs(rs.getDate(18));
			}
			applyCreditsVo.setCreditDetails(getCreditDetailsById(id));
			applyCreditsVo.setInvoiceDetails(getInvoiceDetailsbyId(id));
			List<UploadFileVo> uploadFileVos = new ArrayList<UploadFileVo>();

			for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(id,
					AttachmentsConstants.MODULE_TYPE_ACCOUNTS_RECEIVABLES_APPLY_CREDIT)) {
				UploadFileVo uploadFileVo = new UploadFileVo();
				uploadFileVo.setId(attachments.getId());
				uploadFileVo.setName(attachments.getFileName());
				uploadFileVo.setSize(attachments.getSize());
				uploadFileVo.setDocumentType(attachments.getDocumentTypeId());
				uploadFileVos.add(uploadFileVo);
			}

			applyCreditsVo.setAttachments(uploadFileVos);
		} catch (Exception e) {
			logger.info("Error in  getApplyCreditsById:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return applyCreditsVo;
	}

	private List<ApplyCreditsInvoiceVo> getInvoiceDetailsbyId(int id) throws ApplicationException {
		logger.info("Entry into method: getInvoiceDetailsbyId");
		List<ApplyCreditsInvoiceVo> listOfInvoices = new ArrayList<ApplyCreditsInvoiceVo>();
		Connection con =null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		try {
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(ApplyCreditsConstants.GET_APPLY_CREDIT_INVOICE_DETAILS_BY_ID); 
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ApplyCreditsInvoiceVo applyCreditsInvoiceVo = new ApplyCreditsInvoiceVo();
				applyCreditsInvoiceVo.setId(rs.getInt(1));
				applyCreditsInvoiceVo.setInvoiceId(rs.getInt(2));
				applyCreditsInvoiceVo.setInvoiceAmount(rs.getString(3));
				applyCreditsInvoiceVo.setAppliedAmount(rs.getString(4));
				applyCreditsInvoiceVo.setStatus(rs.getString(5));
				// Processing JSON Data
				String data = rs.getString(6);
				logger.info("Data:" + data);
				if (data != null) {
					JSONParser parser = new JSONParser();
					JSONObject dataJson = (JSONObject) parser.parse(data);
					JSONObject columnsObj = (JSONObject) dataJson.get("data");

					applyCreditsInvoiceVo.setBankCharges(
							columnsObj.get("bankCharges") != null ? columnsObj.get("bankCharges").toString()
									: null);
					applyCreditsInvoiceVo.setTdsDeducted(
							columnsObj.get("tdsDeducted") != null ? columnsObj.get("tdsDeducted").toString()
									: null);
					applyCreditsInvoiceVo.setOthers3(
							columnsObj.get("others3") != null ? columnsObj.get("others3").toString() : null);
					applyCreditsInvoiceVo.setOthers1(
							columnsObj.get("others1") != null ? columnsObj.get("others1").toString() : null);
					applyCreditsInvoiceVo.setOthers2(
							columnsObj.get("others2") != null ? columnsObj.get("others2").toString() : null);

				}
				listOfInvoices.add(applyCreditsInvoiceVo);
			}
		} catch (Exception e) {
			logger.info("Error in  getInvoiceDetailsbyId:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listOfInvoices;
	}

	private List<ApplyCreditDetailsVo> getCreditDetailsById(int id) throws ApplicationException {
		logger.info("Entry into method: getCreditDetailsById");
		Connection con =null;
		PreparedStatement preparedStatement = null;
		ResultSet rs =null;
		List<ApplyCreditDetailsVo> creditDetails = new ArrayList<ApplyCreditDetailsVo>();
		try{
			con = getAccountsReceivableConnection();
			preparedStatement = con.prepareStatement(ApplyCreditsConstants.GET_APPLY_CREDIT_CREDITS_DETAILS_BY_ID);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				ApplyCreditDetailsVo applyCreditDetailsVo = new ApplyCreditDetailsVo();
				applyCreditDetailsVo.setId(rs.getInt(1));
				applyCreditDetailsVo.setReferenceId(rs.getInt(2));
				applyCreditDetailsVo.setCreditType(rs.getString(3));
				applyCreditDetailsVo.setOriginalAmount(rs.getString(4));
				applyCreditDetailsVo.setAvailableAmount(rs.getString(5));
				applyCreditDetailsVo.setAdjustmentAmount(rs.getString(6));
				applyCreditDetailsVo.setStatus(rs.getString(7));
				applyCreditDetailsVo.setLedgerName(rs.getString(8));
				applyCreditDetailsVo.setLedgerId(rs.getObject(9) != null ? rs.getInt(9) : null);
				creditDetails.add(applyCreditDetailsVo);
			}
		} catch (Exception e) {
			logger.info("Error in  getInvoiceDetailsbyId:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return creditDetails;
	}

	public boolean updateApplyCredits(ApplyCreditsVo applyCreditsVo) throws ApplicationException {
		logger.info("Entry into method: updateApplyCredits");
		boolean isTxnSuccess = false;
		Connection con = null;
		if (applyCreditsVo != null) {
			try {
				con = getAccountsReceivableConnection();
				con.setAutoCommit(false);
				boolean isApplyCreditExist=checkApplyCreditExist(applyCreditsVo, con);
				if(isApplyCreditExist){
					throw new Exception("ApplyCredit Voucher No. Exist for the Organization");
				}
				updateApplyCreditsInfo(applyCreditsVo, con);
				if (applyCreditsVo.getCreditDetails() != null && applyCreditsVo.getCreditDetails().size() > 0) {
					for (ApplyCreditDetailsVo applyCreditDetailsVo : applyCreditsVo.getCreditDetails()) {
						if (applyCreditDetailsVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_NEW)) {
							applyCreditDetailsVo.setStatus(CommonConstants.STATUS_AS_ACTIVE);
							createCreditDetails(applyCreditDetailsVo, applyCreditsVo.getId(), con);
						} else {
							updateCreditDetails(applyCreditDetailsVo, applyCreditsVo.getId(), con);
						}
					}
				}

				if (applyCreditsVo.getInvoiceItemsToRemove() != null
						&& applyCreditsVo.getInvoiceItemsToRemove().size() > 0) {
					String query = ApplyCreditsConstants.DELETE_INVOICE_TABLE_ENTRIES;
					for (int id : applyCreditsVo.getInvoiceItemsToRemove()) {
						deleteChildEntries(id, con, query);
					}

				}
				if (applyCreditsVo.getCreditsItemsToRemove() != null
						&& applyCreditsVo.getCreditsItemsToRemove().size() > 0) {
					String query = ApplyCreditsConstants.DELETE_CREDIT_TABLE_ENTRIES;
					for (int id : applyCreditsVo.getCreditsItemsToRemove()) {
						deleteChildEntries(id, con, query);
					}
				}

				if (applyCreditsVo.getInvoiceDetails() != null && applyCreditsVo.getInvoiceDetails().size() > 0) {
					for (ApplyCreditsInvoiceVo applyCreditsInvoiceVo : applyCreditsVo.getInvoiceDetails()) {
						if (applyCreditsInvoiceVo.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_NEW)) {
							applyCreditsInvoiceVo.setStatus(CommonConstants.STATUS_AS_ACTIVE);
							createInvoiceDetails(applyCreditsInvoiceVo, applyCreditsVo.getId(), con);
						} else {
							updateInvoiceDetails(applyCreditsInvoiceVo, applyCreditsVo.getId(), con);
						}
					}
				}

				if (applyCreditsVo.getAttachments() != null && applyCreditsVo.getAttachments().size() > 0) {
					attachmentsDao.createAttachments(applyCreditsVo.getOrganizationId(), applyCreditsVo.getUserId(),
							applyCreditsVo.getAttachments(),
							AttachmentsConstants.MODULE_TYPE_VENDOR_PORTAL_BALANCE_CONFIRMATION, applyCreditsVo.getId(),
							applyCreditsVo.getRoleName());
				}

				if (applyCreditsVo.getAttachmentsToRemove() != null
						&& applyCreditsVo.getAttachmentsToRemove().size() > 0) {
					for (Integer attachmentId : applyCreditsVo.getAttachmentsToRemove()) {
						attachmentsDao.changeStatusForSingleAttachment(attachmentId, CommonConstants.STATUS_AS_DELETE,
								applyCreditsVo.getUserId(), applyCreditsVo.getRoleName());
					}
				}
				con.commit();
				isTxnSuccess = true;
			} catch (Exception e) {
				logger.info("Error in  updateApplyCredits:", e);
				try {
					con.rollback();
				} catch (SQLException e1) {
					throw new ApplicationException(e1.getMessage());
				}
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(null, null, con);
			}
		}
		return isTxnSuccess;
	}

	private void deleteChildEntries(int id, Connection con, String query) throws ApplicationException {
		logger.info("To delete the child entries deleteChildEntries :: " + query);
		if (query != null) {
			PreparedStatement preparedStatement = null;
			try  {
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setInt(1, id);
				preparedStatement.executeUpdate();
				logger.info("Recored deleted successfully");
			} catch (Exception e) {
				logger.info("Error in deleteChildEntries ", e);
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(null, preparedStatement, null);
			}
		}
	}

	private void updateApplyCreditsInfo(ApplyCreditsVo applyCreditsVo, Connection con) throws ApplicationException {
		logger.info("Entry into method: updateApplyCreditsInfo");
		PreparedStatement preparedStatement =null;
		try  {
			preparedStatement = con.prepareStatement(ApplyCreditsConstants.UPDATE_APPLY_CREDITS);
			preparedStatement.setString(1,
					applyCreditsVo.getVoucherNo() != null ? applyCreditsVo.getVoucherNo() : null);
			preparedStatement.setObject(2, applyCreditsVo.getDate() != null ? applyCreditsVo.getDate() : null);
			preparedStatement.setObject(3,
					applyCreditsVo.getCustomerId() != null ? applyCreditsVo.getCustomerId() : null);
			preparedStatement.setObject(4, applyCreditsVo.getLedgerId() != null ? applyCreditsVo.getLedgerId() : null);
			preparedStatement.setString(5,
					applyCreditsVo.getLedgerName() != null ? applyCreditsVo.getLedgerName() : null);
			preparedStatement.setObject(6,
					applyCreditsVo.getCurrencyId() != null ? applyCreditsVo.getCurrencyId() : null);
			preparedStatement.setString(7,
					applyCreditsVo.getInvoiceTotalAmount() != null ? applyCreditsVo.getInvoiceTotalAmount() : null);
			preparedStatement.setString(8,
					applyCreditsVo.getAdjustedCreditAmount() != null ? applyCreditsVo.getAdjustedCreditAmount() : null);
			preparedStatement.setString(9,
					applyCreditsVo.getAvailableFund() != null ? applyCreditsVo.getAvailableFund() : null);
			preparedStatement.setObject(10,
					applyCreditsVo.getOrganizationId() != null ? applyCreditsVo.getOrganizationId() : null);
			preparedStatement.setString(11, applyCreditsVo.getStatus() != null ? applyCreditsVo.getStatus() : null);
			preparedStatement.setObject(12,
					applyCreditsVo.getIsSuperAdmin() != null ? applyCreditsVo.getIsSuperAdmin() : null);
			preparedStatement.setObject(13,
					applyCreditsVo.getUserId() != null ? Integer.valueOf(applyCreditsVo.getUserId()) : null);
			List<ReceiptSettingsVo> customTableList = applyCreditsVo.getCustomTableList();
			ObjectMapper mapper = new ObjectMapper();
			String newJsonData = mapper.writeValueAsString(customTableList);
			preparedStatement.setString(14, newJsonData != null ? newJsonData : null);
			preparedStatement.setString(15, applyCreditsVo.getRoleName() != null ? applyCreditsVo.getRoleName() : null);
			preparedStatement.setString(16,
					applyCreditsVo.getExchangeRate() != null ? applyCreditsVo.getExchangeRate() : null);
			preparedStatement.setInt(17, applyCreditsVo.getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("Error in  updateApplyCreditsInfo:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, null);
		}
	}

	private void updateInvoiceDetails(ApplyCreditsInvoiceVo applyCreditsInvoiceVo, Integer id, Connection con)
			throws ApplicationException {
		logger.info("Entry into method: updateInvoiceDetails");
		PreparedStatement preparedStatement =null;
		try{
			preparedStatement = con.prepareStatement(ApplyCreditsConstants.UPDATE_APPLY_CREDITS_INVOICE_DETAILS);
			preparedStatement.setObject(1,
					applyCreditsInvoiceVo.getInvoiceId() != null ? applyCreditsInvoiceVo.getInvoiceId() : null);
			preparedStatement.setString(2,
					applyCreditsInvoiceVo.getInvoiceAmount() != null ? applyCreditsInvoiceVo.getInvoiceAmount() : null);
			preparedStatement.setString(3,
					applyCreditsInvoiceVo.getAppliedAmount() != null ? applyCreditsInvoiceVo.getAppliedAmount() : null);
			preparedStatement.setString(4,
					applyCreditsInvoiceVo.getStatus() != null ? applyCreditsInvoiceVo.getStatus() : null);
			String data = "{\"data\":";
			data += "{\n" + "\"bankCharges\":\"" + applyCreditsInvoiceVo.getBankCharges() + "\"," + "\"tdsDeducted\":\""
					+ applyCreditsInvoiceVo.getTdsDeducted() + "\"," + "\"others3\":\""
					+ applyCreditsInvoiceVo.getOthers3() + "\"," + "\"others1\":\"" + applyCreditsInvoiceVo.getOthers1()
					+ "\"," + "\"others2\":\"" + applyCreditsInvoiceVo.getOthers2() + "\"" + "}" + "}";
			logger.info(data);
			preparedStatement.setString(5, data);
			preparedStatement.setInt(6, applyCreditsInvoiceVo.getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("Error in  updateInvoiceDetails:", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, null);
		}
	}

	private void updateCreditDetails(ApplyCreditDetailsVo applyCreditDetailsVo, Integer id, Connection con)
			throws ApplicationException {
		logger.info("Entry into method: updateCreditDetails");
		PreparedStatement preparedStatement=null;
		try  {
			preparedStatement = con.prepareStatement(ApplyCreditsConstants.UPDATE_APPLY_CREDITS_CREDIT_DETAILS);
			preparedStatement.setObject(1,
					applyCreditDetailsVo.getReferenceId() != null ? applyCreditDetailsVo.getReferenceId() : null);
			preparedStatement.setString(2,
					applyCreditDetailsVo.getCreditType() != null ? applyCreditDetailsVo.getCreditType() : null);
			preparedStatement.setString(3,
					applyCreditDetailsVo.getOriginalAmount() != null ? applyCreditDetailsVo.getOriginalAmount() : null);
			preparedStatement.setString(4,
					applyCreditDetailsVo.getAvailableAmount() != null ? applyCreditDetailsVo.getAvailableAmount()
							: null);
			preparedStatement.setString(5,
					applyCreditDetailsVo.getAdjustmentAmount() != null ? applyCreditDetailsVo.getAdjustmentAmount()
							: null);
			preparedStatement.setString(6,
					applyCreditDetailsVo.getStatus() != null ? applyCreditDetailsVo.getStatus() : null);
			preparedStatement.setObject(7,
					applyCreditDetailsVo.getLedgerId() != null ? applyCreditDetailsVo.getLedgerId() : null);
			preparedStatement.setString(8,
					applyCreditDetailsVo.getLedgerName() != null ? applyCreditDetailsVo.getLedgerName() : null);
			preparedStatement.setInt(9, applyCreditDetailsVo.getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			logger.info("Error in updateCreditDetails::", e);
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(null, preparedStatement, null);
		}
	}

	public void deleteApplyCreditsEntries(Integer id, String userId, String roleName) throws ApplicationException {
		logger.info("To deleteApplyCreditsEntries:: ");
		Connection connection = null;
		if (id != null) {
			try {
				connection = getAccountsPayable();
				// To remove from apply credit main table
				changeStatusForApplyCreditTable(id, CommonConstants.STATUS_AS_DELETE, connection,
						ApplyCreditsConstants.MODIFY_APPLY_CREDIT_STATUS);
				// To remove from Attachments table
				attachmentsDao.changeStatusForAttachments(id, CommonConstants.STATUS_AS_DELETE,
						AttachmentsConstants.MODULE_TYPE_VENDOR_PORTAL_BALANCE_CONFIRMATION, userId, roleName);
				logger.info("Deleted successfully in all tables ");
			} catch (Exception e) {
				logger.info("Error in deleteApplyCreditsEntries:: ", e);
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(null, null, connection);
			}
		}

	}

	private boolean checkApplyCreditExist(ApplyCreditsVo applyCreditsVo,Connection conn) throws ApplicationException {
		logger.info("Entry into method: checkApplyCreditExist");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			if(applyCreditsVo!=null&&applyCreditsVo.getVoucherNo()!=null ) {
				String query = ApplyCreditsConstants.CHECK_AC_ORGANIZATION;
				preparedStatement = conn.prepareStatement(query);
				preparedStatement.setInt(1, applyCreditsVo.getOrganizationId());
				preparedStatement.setString(2, applyCreditsVo.getVoucherNo());
				rs = preparedStatement.executeQuery();
				if (rs.next()) {
					if(applyCreditsVo.getId()==null) {//create scenario
						return true;	
					}else {
						if(applyCreditsVo.getId()!=rs.getInt(1)) {//If it is not same record
							return true;
						}
					}

				}
			}
		} catch (Exception e) {
			logger.error("Error in checkApplyCreditExist",e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return false;
	}


	private void changeStatusForApplyCreditTable(Integer id, String status, Connection con, String query)
			throws ApplicationException {
		logger.info("To Change the status with query :: " + query);
		PreparedStatement preparedStatement =null;
		if (query != null) {
			try  {
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setString(1, status);
				preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
				preparedStatement.setInt(3, id);
				preparedStatement.executeUpdate();
				logger.info("Status chaneged successfully ");
			} catch (Exception e) {
				logger.info("Error in changeStatusForApplyCreditTable ", e);
				throw new ApplicationException(e.getMessage());
			} finally {
				closeResources(null, preparedStatement, null);
			}

		}
	}
}
