package com.blackstrawai.ap;

import java.sql.Connection;
import java.sql.Date;
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
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.DateConverter;
import com.blackstrawai.EnglishNumberToWords;
import com.blackstrawai.ap.billsinvoice.InvoiceListVo;
import com.blackstrawai.ap.dropdowns.BasicVoucherEntriesVo;
import com.blackstrawai.ap.dropdowns.PaymentModeDropDownVo;
import com.blackstrawai.ap.dropdowns.PaymentNonCoreBillDetailsDropDownVo;
import com.blackstrawai.ap.dropdowns.PaymentNonCoreGstDetailsDropDownVo;
import com.blackstrawai.ap.dropdowns.PaymentTypeVo;
import com.blackstrawai.ap.payment.noncore.ContactAddressVo;
import com.blackstrawai.ap.payment.noncore.CreditDetailsVo;
import com.blackstrawai.ap.payment.noncore.ListPaymentNonCoreVo;
import com.blackstrawai.ap.payment.noncore.NameVo;
import com.blackstrawai.ap.payment.noncore.PaymentAdviceVo;
import com.blackstrawai.ap.payment.noncore.PaymentNonCoreBaseVo;
import com.blackstrawai.ap.payment.noncore.PaymentNonCoreCustomTableVo;
import com.blackstrawai.ap.payment.noncore.PaymentNonCoreVo;
import com.blackstrawai.ar.dropdowns.PaymentModeVo;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.attachments.AttachmentsDao;
import com.blackstrawai.attachments.AttachmentsVo;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.journals.GeneralLedgerVo;
import com.blackstrawai.onboarding.OrganizationDao;
import com.blackstrawai.settings.TaxRateMappingVo;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class PaymentNonCoreDao extends BaseDao {

	private Logger logger = Logger.getLogger(PaymentNonCoreDao.class);

	@Autowired
	private AttachmentsDao attachmentsDao;
//	@Autowired
//	private BankMasterDao bankMasterDao;
//	@Autowired
//	private StatutoryBodyDao statutoryBodyDao;
//	@Autowired
//	private CustomerDao customerDao;
//	@Autowired
//	private VendorDao vendorDao;
	@Autowired
	@Lazy
	private OrganizationDao organizationDao;
//	@Autowired
//	private FinanceCommonDao financeCommonDao;
//	@Autowired
//	private CurrencyDao currencyDao;

	public PaymentNonCoreVo createPayment(PaymentNonCoreVo paymentVo) throws ApplicationException, SQLException {

		logger.info("Entry into method: createPayment");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getAccountsPayable();
			con.setAutoCommit(false);
			String sql = PaymentNonCoreConstants.INSERT_INTO_PAYMENT_NON_CORE;
			preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			String paymentRefNo = formatPaymentRefNo(paymentVo.getPaymentRefNo(), paymentVo.getOrganizationId());
			boolean isPaymentExist = checkPaymentExist(paymentRefNo, paymentVo.getOrganizationId(), con);
			if (isPaymentExist) {
				throw new ApplicationException("Payment already exists for the organization");
			}
			preparedStatement.setInt(1, paymentVo.getPaidVia());
			preparedStatement.setInt(2, paymentVo.getPaymentType());
			preparedStatement.setString(3, paymentRefNo);
			String date = paymentVo.getPaymentDate() != null
					? DateConverter.getInstance().correctDatePickerDateToString(paymentVo.getPaymentDate())
					: null;
			preparedStatement.setString(4, date);
//			preparedStatement.setString(4,
//					paymentVo.getPaymentDate() != null
//							? DateConverter.getInstance().correctDatePickerDateToString(paymentVo.getPaymentDate())
//							: null);
			preparedStatement.setInt(5, paymentVo.getVendor());
			preparedStatement.setInt(6, paymentVo.getVendorAccountId());
			preparedStatement.setString(7, paymentVo.getVendorAccountName());
			preparedStatement.setInt(8, paymentVo.getCurrency());
			preparedStatement.setString(9, paymentVo.getNotes());
			preparedStatement.setString(10, paymentVo.getAdjustedAmount());
			preparedStatement.setString(11, paymentVo.getTotalAmount());
			preparedStatement.setString(12, paymentVo.getPoRefNo());
			preparedStatement.setInt(13, paymentVo.getContactAccountId());
			preparedStatement.setString(14, paymentVo.getContactAccountName());
			preparedStatement.setInt(15, paymentVo.getContactId());
			preparedStatement.setString(16, paymentVo.getAmountPaid());
			preparedStatement.setString(17, paymentVo.getDifferenceAmount());
			preparedStatement.setInt(18, paymentVo.getPayType());
			preparedStatement.setString(19, paymentVo.getPayPeriod());
			preparedStatement.setString(20, paymentVo.getPayRunRefNo());
			preparedStatement.setString(21, paymentVo.getUserId());
			preparedStatement.setInt(22, paymentVo.getOrganizationId());
			preparedStatement.setString(23, paymentVo.getRoleName());
			preparedStatement.setBoolean(24, paymentVo.isSuperAdmin());
			preparedStatement.setString(25, paymentVo.getStatus());
			preparedStatement.setString(26, paymentVo.getPaidTo());
			preparedStatement.setTimestamp(27, new Timestamp(System.currentTimeMillis()));

			ObjectMapper mapper = new ObjectMapper();
			List<PaymentNonCoreCustomTableVo> customTableList = paymentVo.getCustomTableList();
			if (customTableList != null)
				logger.info(customTableList.size());
			String newJsonData = mapper.writeValueAsString(customTableList);
			logger.info(newJsonData);
			preparedStatement.setString(28, newJsonData);
			preparedStatement.setInt(29, paymentVo.getPaymentMode());
			preparedStatement.setString(30, paymentVo.getContactType());
			preparedStatement.setInt(31, paymentVo.getStatutoryBody());
			preparedStatement.setInt(32, paymentVo.getCustomerName());
			preparedStatement.setInt(33, paymentVo.getEmployeeName());
			preparedStatement.setInt(34, paymentVo.getPaidFor());
			preparedStatement.setString(35, paymentVo.getCurrencyCode());
			preparedStatement.setString(36, paymentVo.getExchangeRate());
			preparedStatement.setInt(37, paymentVo.getBillRef());
			preparedStatement.setInt(38, paymentVo.getReferenceId());
			preparedStatement.setInt(39, paymentVo.getInvoiceId());
			preparedStatement.setString(40, paymentVo.getReferenceType());
			preparedStatement.setBoolean(41, paymentVo.isBulk());
			preparedStatement.setString(42, paymentVo.getPaidViaName());
			preparedStatement.setString(43, paymentVo.getContactName());
			preparedStatement.setString(44, paymentVo.getInvRefName());
			preparedStatement.setString(45, paymentVo.getBaseCurrencyCode());
			String glData = paymentVo.getGeneralLedgerData() != null
					? mapper.writeValueAsString(paymentVo.getGeneralLedgerData())
					: null;
			logger.info("glData>> " + glData);
			preparedStatement.setString(46, glData);
			if (paymentVo.getDisplayDueAmount() != null && paymentVo.getDisplayDueAmount().length() > 0)
				preparedStatement.setDouble(47, Double.parseDouble(paymentVo.getDisplayDueAmount()));
			else
				preparedStatement.setDouble(47, 0.0);
			preparedStatement.setString(48, paymentVo.getBankReferenceNumber());
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();

				if (rs.next()) {
					paymentVo.setId(rs.getInt(1));
				}

				if (paymentVo.getPayments() != null && paymentVo.getPayments().size() > PaymentNonCoreConstants.ZERO) {
					createChildTables(paymentVo);
				}
				paymentVo.setPaymentRefNo(paymentRefNo);
				if (getPaymentTypeById(paymentVo.getPaymentType()).equals(PaymentNonCoreConstants.VENDOR_ADVANCE)) {
					createVendorAdvance(paymentVo, con);
				}

				if (paymentVo.getAttachments() != null) {
					attachmentsDao.createAttachments(paymentVo.getOrganizationId(), paymentVo.getUserId(),
							paymentVo.getAttachments(), AttachmentsConstants.MODULE_TYPE_AP_PAYMENTS, paymentVo.getId(),
							paymentVo.getRoleName());
				}

			}
			con.commit();

		} catch (Exception e) {
			logger.error("Error during createPayment ", e);
			con.rollback();
			logger.info("Before throw in dao :: " + e.getMessage());
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return paymentVo;
	}

	private boolean checkPaymentExist(String paymentRefNo, int organizationId, Connection conn)
			throws ApplicationException {

		logger.info("Entry into method: checkPaymentExist");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {

			if (paymentRefNo != null) {
				String query = PaymentNonCoreConstants.CHECK_PAYMENT_FOR_ORGANIZATION;
				preparedStatement = conn.prepareStatement(query);
				preparedStatement.setInt(1, organizationId);
				preparedStatement.setString(2, paymentRefNo);

				rs = preparedStatement.executeQuery();
				if (rs.next()) {
					return true;
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}
		return false;

	}

	private String formatPaymentRefNo(String paymentRefNo, int organizationId) throws ApplicationException {
		Connection con = getUserMgmConnection();
		BasicVoucherEntriesVo paymentVoucher = organizationDao.getBasicVoucherEntries(organizationId, con,
				PaymentNonCoreConstants.PAYMENTS);
		closeResources(null, null, con);
		String completePaymentRefNo = paymentRefNo;
		if (paymentVoucher != null) {
			completePaymentRefNo = "";
			if (paymentVoucher.getPrefix() != null && paymentVoucher.getPrefix().length() > 0) {
				completePaymentRefNo += paymentVoucher.getPrefix() + "/";
			}
			completePaymentRefNo += paymentRefNo;
			if (paymentVoucher.getSuffix() != null && paymentVoucher.getSuffix().length() > 0) {
				completePaymentRefNo += "/" + paymentVoucher.getSuffix();
			}
		}
		return completePaymentRefNo;
	}

	private void createChildTables(PaymentNonCoreVo paymentVo) throws ApplicationException {
		for (PaymentNonCoreBaseVo paymentChild : paymentVo.getPayments()) {
			paymentChild.setPaymentRefId(paymentVo.getId());
		}
		Connection con = getAccountsPayable();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			switch (getPaymentTypeById(paymentVo.getPaymentType())) {
			case PaymentNonCoreConstants.BILL_PAYMENTS:
				createBillDetails(paymentVo, con);
				break;
			case PaymentNonCoreConstants.GST_PAYMENTS:
				createGstDetails(paymentVo, con);
				break;
			case PaymentNonCoreConstants.TDS_PAYMENTS:
				createTdsDetails(paymentVo, con);
				break;
			case PaymentNonCoreConstants.CUSTOMER_REFUNDS:
				createCustomerRefunds(paymentVo, con);
				break;
			case PaymentNonCoreConstants.EMPLOYEE_PAYMENTS:
				createEmployeeDetails(paymentVo, con);
				break;
			}
		} catch (Exception e) {
			logger.error("Error during createChildTables ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

	}

	private void createVendorAdvance(PaymentNonCoreVo paymentVo, Connection con) throws ApplicationException {

		logger.info("Entry into method: createVendorAdvance");
		PreparedStatement preparedStatement = null;
		try {
			String query = PaymentNonCoreConstants.INSERT_VENDOR_ADVANCE;
			logger.info(query);
			preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			preparedStatement.setString(1, paymentVo.getPaymentRefNo());
			preparedStatement.setString(2, paymentVo.getAmountPaid());
			if (paymentVo.getAmountPaid() != null && paymentVo.getAmountPaid().length() > 0)
				preparedStatement.setDouble(3, Double.parseDouble(paymentVo.getAmountPaid()));
			preparedStatement.setInt(4, paymentVo.getVendor());
			preparedStatement.setInt(5, paymentVo.getOrganizationId());
			preparedStatement.setInt(6, paymentVo.getId());

			preparedStatement.executeUpdate();

		} catch (Exception e) {
			logger.error("Error during createVendorAdvance ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatement, null);
		}

	}

	private void createEmployeeDetails(PaymentNonCoreVo paymentVo, Connection con) throws ApplicationException {

		logger.info("Entry into method: createEmployeeDetails");
		List<PaymentNonCoreBaseVo> payments = paymentVo.getPayments();
		PreparedStatement preparedStatement = null;
		try {
			String query = PaymentNonCoreConstants.INSERT_EMPLOYEE_DETAILS;
			logger.info(query);
			preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			for (PaymentNonCoreBaseVo payment : payments) {
				preparedStatement.setInt(1, paymentVo.getId());
				preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
				preparedStatement.setString(3, payment.getEmpName());
				preparedStatement.setString(4, payment.getNetPay());

				String data = "{\"data\":";
				data += "{\n" + "\"payable\":\"" + payment.getPayable() + "\"," + "\"advance\":\""
						+ payment.getAdvance() + "\"," + "\"others1\":\"" + payment.getOthers1() + "\","
						+ "\"others2\":\"" + payment.getOthers2() + "\"," + "\"others3\":\"" + payment.getOthers3()
						+ "\"" + "}" + "}";

				preparedStatement.setString(5, data);
				preparedStatement.setInt(6, payment.getPayRun());
				if (payment.getDueAmount() != null && payment.getDueAmount().length() > 0)
					preparedStatement.setDouble(7, Double.parseDouble(payment.getDueAmount()));
				else
					preparedStatement.setDouble(7, 0.0);

				preparedStatement.executeUpdate();
			}

		} catch (Exception e) {
			logger.error("Error during createEmployeeDetails ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatement, null);
		}

	}

	private void createCustomerRefunds(PaymentNonCoreVo paymentVo, Connection con) throws ApplicationException {

		logger.info("Entry into method: createCustomerRefunds");
		List<PaymentNonCoreBaseVo> payments = paymentVo.getPayments();
		PreparedStatement preparedStatement = null;
		try {
			String query = PaymentNonCoreConstants.INSERT_CUSTOMER_REFUND;
			logger.info(query);
			preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			for (PaymentNonCoreBaseVo payment : payments) {
				preparedStatement.setInt(1, paymentVo.getId());
				preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
				preparedStatement.setInt(3, payment.getInvoiceId());
				String refundAmount = payment.getInvoiceAmount();
				if (!paymentVo.isBulk()) {
					refundAmount = paymentVo.getAmountPaid();
				}
				preparedStatement.setString(4, refundAmount);
				preparedStatement.setInt(5, payment.getReferenceId());
				preparedStatement.setString(6, payment.getReferenceType());
				preparedStatement.setString(7, payment.getInvRefName());
//				preparedStatement.setDouble(8, Double.parseDouble(payment.getDueAmount()));

				preparedStatement.executeUpdate();
			}

		} catch (Exception e) {
			logger.error("Error during createCustomerRefunds ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatement, null);
		}

	}

	private void createTdsDetails(PaymentNonCoreVo paymentVo, Connection con) throws ApplicationException {

		logger.info("Entry into method: createTdsDetails");
		List<PaymentNonCoreBaseVo> payments = paymentVo.getPayments();
		PreparedStatement preparedStatement = null;
		try {
			String query = PaymentNonCoreConstants.INSERT_TDS_DETAILS;
			logger.info(query);
			preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			for (PaymentNonCoreBaseVo payment : payments) {
				preparedStatement.setInt(1, paymentVo.getId());
				preparedStatement.setString(2, paymentVo.getStatus());
				preparedStatement.setString(3, payment.getTdsSection());
				preparedStatement.setString(4, payment.getType());
				preparedStatement.setString(5, payment.getPaidAmt());

				String data = "{\"data\":";
				data += "{\n" + "\"tax\":\"" + payment.getTax() + "\"," + "\"interest\":\"" + payment.getInterest()
						+ "\"," + "\"penalty\":\"" + payment.getPenalty() + "\"," + "\"others1\":\""
						+ payment.getOthers1() + "\"," + "\"others2\":\"" + payment.getOthers2() + "\","
						+ "\"taxLedgerId\":\"" + paymentVo.getTax() + "\"," + "\"interestLedgerId\":\""
						+ paymentVo.getInterest() + "\"," + "\"penaltyLedgerId\":\"" + paymentVo.getPenalty() + "\""
						+ "}" + "}";

				preparedStatement.setString(6, data);

				preparedStatement.executeUpdate();
			}

		} catch (Exception e) {
			logger.error("Error during createTdsDetails ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatement, null);
		}

	}

	private void createGstDetails(PaymentNonCoreVo paymentVo, Connection con) throws ApplicationException {

		logger.info("Entry into method: createGstDetails");
		List<PaymentNonCoreBaseVo> payments = paymentVo.getPayments();
		PreparedStatement preparedStatement = null;
		try {
			String query = PaymentNonCoreConstants.INSERT_GST_DETAILS;
			logger.info(query);
			preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			for (PaymentNonCoreBaseVo payment : payments) {
				preparedStatement.setInt(1, paymentVo.getId());
				preparedStatement.setString(2, paymentVo.getStatus());
				preparedStatement.setString(3, payment.getGstType());
				preparedStatement.setString(4, payment.getPaidAmt());

				String data = "{\"data\":";
				data += "{\n" + "\"tax\":\"" + payment.getTax() + "\"," + "\"interest\":\"" + payment.getInterest()
						+ "\"," + "\"penalty\":\"" + payment.getPenalty() + "\"," + "\"others1\":\""
						+ payment.getOthers1() + "\"," + "\"others2\":\"" + payment.getOthers2() + "\","
						+ "\"taxLedgerId\":\"" + paymentVo.getTax() + "\"," + "\"interestLedgerId\":\""
						+ paymentVo.getInterest() + "\"," + "\"penaltyLedgerId\":\"" + paymentVo.getPenalty() + "\""
						+ "}" + "}";

				preparedStatement.setString(5, data);

				preparedStatement.executeUpdate();
			}

		} catch (Exception e) {
			logger.error("Error during createGstDetails ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatement, null);
		}

	}

	public void createBillDetails(PaymentNonCoreVo paymentVo, Connection con) throws ApplicationException {
		logger.info("Entry into method: createBillDetails");
		List<PaymentNonCoreBaseVo> payments = paymentVo.getPayments();
		PreparedStatement preparedStatement = null;
		try {
			String query = PaymentNonCoreConstants.INSERT_BILL_DETAILS;
			logger.info(query);
			preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			for (PaymentNonCoreBaseVo payment : payments) {
				preparedStatement.setInt(1, paymentVo.getId());
				preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
				preparedStatement.setString(3, payment.getBillRef());
				preparedStatement.setString(4, payment.getBillAmount());
				preparedStatement.setString(5, payment.getTotalAmount());

				String data = "{\"data\":";
				data += "{\n" + "\"dueAmount\":\"" + payment.getDueAmount() + "\"," + "\"dueDate\":\""
						+ payment.getDueDate() + "\"," + "\"bankFees\":\"" + payment.getBankCharges() + "\","
						+ "\"tdsDeductions\":\"" + payment.getTdsDeducted() + "\"," + "\"others1\":\""
						+ payment.getOthers1() + "\"," + "\"others2\":\"" + payment.getOthers2() + "\","
						+ "\"dueAmountLedgerId\":\"" + paymentVo.getDueAmount() + "\"," + "\"bankFeesLedgerId\":\""
						+ paymentVo.getBankFees() + "\"," + "\"tdsDeductionsLedgerId\":\""
						+ paymentVo.getTdsDeductions() + "\"" + "}" + "}";

				preparedStatement.setString(6, data);
				preparedStatement.setString(7, payment.getType());
				preparedStatement.setInt(8, payment.getVendorId());
				preparedStatement.executeUpdate();
			}

		} catch (Exception e) {
			logger.error("Error during createBillDetails ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatement, null);
		}
	}

	public List<ListPaymentNonCoreVo> getAllPaymentsOfAnOrganizationForUserAndRole(int organizationId, String userId,
			String roleName) throws ApplicationException, SQLException {

		logger.info("Entry into method: getAllPaymentsOfAnOrganizationForUserAndRole");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<ListPaymentNonCoreVo> totalList = new ArrayList<ListPaymentNonCoreVo>();
		try {
			con = getAccountsPayable();
			String query = "";

			if (roleName.equals("Super Admin")) {
				query = PaymentNonCoreConstants.GET_PAYMENTS_OF_ORG;
			} else {
				query = PaymentNonCoreConstants.GET_PAYMENTS_OF_ORG_USER_ROLE;
			}
			logger.info(query);
			preparedStatement = con.prepareStatement(query);

			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, CommonConstants.STATUS_AS_DELETE);
			preparedStatement.setString(3, CommonConstants.STATUS_AS_INACTIVE);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_VOID);
			if (!(roleName.equals("Super Admin"))) {
				preparedStatement.setString(5, userId);
				preparedStatement.setString(6, roleName);
			}
			rs = preparedStatement.executeQuery();
			totalList = generatePaymentList(rs);

		} catch (Exception e) {
			logger.error("Error during getAllPaymentsOfAnOrganizationForUserAndRole", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

		return totalList;

	}

	private List<ListPaymentNonCoreVo> generatePaymentList(ResultSet rs) throws SQLException {
		List<ListPaymentNonCoreVo> totalList = new ArrayList<>();
		while(rs.next()) {

			ListPaymentNonCoreVo listVo = new ListPaymentNonCoreVo();
			listVo.setId(rs.getInt("id"));
			String dateFormat = rs.getString("date_format");
			if (dateFormat != null && dateFormat.length() > 0) {
				listVo.setPaymentDate(rs.getDate("date") != null
						? DateConverter.getInstance().convertDateToGivenFormat(rs.getDate("date"), dateFormat)
						: null);
			}

			listVo.setPaymentRefNo(rs.getString("pay_ref"));
			listVo.setPaymentType(rs.getString("type"));
			listVo.setPaymentModeId(rs.getInt("mode_id"));
			listVo.setPaidViaId(rs.getInt("paid_via_id"));

			String paymentMode = rs.getString("mode");
			if (null != paymentMode && paymentMode.equalsIgnoreCase("Cash")) {
				listVo.setPaidVia(rs.getString("cash_name"));
			} else if (null != paymentMode && (paymentMode.equalsIgnoreCase("Bank Transfer")
					|| paymentMode.equalsIgnoreCase("Cheque&Demand Draft"))) {
				listVo.setPaidVia(rs.getString("bank_name"));
			} else if (null != paymentMode && paymentMode.equalsIgnoreCase("Wallet")) {
				listVo.setPaidVia(rs.getString("wallet_name"));
			} else if (null != paymentMode && paymentMode.equalsIgnoreCase("Credit Card")) {
				listVo.setPaidVia(rs.getString("account_name"));
			}

			String contactType = rs.getString("contact_type");
			List<NameVo> names = new ArrayList<>();
			switch (listVo.getPaymentType()) {
			case PaymentNonCoreConstants.TDS_PAYMENTS:
			case PaymentNonCoreConstants.GST_PAYMENTS:
				names.add(new NameVo(rs.getString("statutory")));
				listVo.setContact(names);
				listVo.setContactId(rs.getInt("statutory_id"));
				break;
			case PaymentNonCoreConstants.CUSTOMER_REFUNDS:
				names.add(new NameVo(rs.getString("customer")));
				listVo.setContact(names);
				listVo.setContactId(rs.getInt("customer_id"));
				break;
			case PaymentNonCoreConstants.BILL_PAYMENTS:
			case PaymentNonCoreConstants.VENDOR_ADVANCE:
				names.add(new NameVo(rs.getString("vendor")));
				listVo.setContact(names);
				listVo.setContactId(rs.getInt("vendor_id"));
				break;
			case PaymentNonCoreConstants.EMPLOYEE_PAYMENTS:
				String empString = rs.getString("emp_name");
				if (empString != null && empString.length() > 0) {
					String[] empArray = empString.split(",");
					for (String emp : empArray) {
						names.add(new NameVo(emp));
					}
				}
				listVo.setContact(names);
				break;
			case PaymentNonCoreConstants.OTHERS:
				if (contactType != null) {
					switch (contactType) {
					case PaymentNonCoreConstants.STATUTORY_BODY:
						names.add(new NameVo(rs.getString("statutory")));
						listVo.setContact(names);
						listVo.setContactId(rs.getInt("statutory_id"));
						break;
					case PaymentNonCoreConstants.CUSTOMER:
						names.add(new NameVo(rs.getString("customer")));
						listVo.setContact(names);
						listVo.setContactId(rs.getInt("customer_id"));
						break;
					case PaymentNonCoreConstants.EMPLOYEE:
						names.add(new NameVo(rs.getString("emp_name")));
						listVo.setContact(names);
						listVo.setContactId(rs.getInt("contact_id"));
						break;
					case PaymentNonCoreConstants.VENDOR:
						names.add(new NameVo(rs.getString("vendor")));
						listVo.setContact(names);
						listVo.setContactId(rs.getInt("vendor_id"));
						break;
					}
				}
			}

			String amountPaid = rs.getString("amount_paid");
			listVo.setAmountWithoutSymbol(amountPaid);

			listVo.setCurrencyId(rs.getInt("currency_id"));
			String currencySymbol = rs.getString("symbol");
			if (amountPaid != null && amountPaid.length() > 0 && currencySymbol != null) {// Set symbol
				listVo.setAmount(currencySymbol + " " + amountPaid);
			} else {
				listVo.setAmount(amountPaid);
			}
			String status = rs.getString("status");
			String displayStatus = "";
			if (status != null) {
				switch (status) {
				case CommonConstants.STATUS_AS_ACTIVE:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_ACTIVE;
					break;
				case CommonConstants.STATUS_AS_VOID:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_VOID;
					break;
				case CommonConstants.STATUS_AS_DRAFT:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_DRAFT;
					break;
				case CommonConstants.STATUS_AS_ADJUSTED:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_ADJUSTED;
					break;
				case CommonConstants.STATUS_AS_PARTIALLY_ADJUSTED:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_PARTIALLY_ADJUSTED;
					break;
				case CommonConstants.STATUS_AS_INACTIVE:
					displayStatus = CommonConstants.DISPLAY_STATUS_AS_INACTIVE;
					break;

				}
			}
			listVo.setStatus(displayStatus);
			listVo.setContactType(rs.getString("contact_type"));
			listVo.setPaymentModeId(rs.getInt("mode_id"));
			String invoiceString = rs.getString("invoices");
			if (invoiceString != null)
				invoiceString = invoiceString.replace("~", "/");
			String advanceString = rs.getString("advance");
			String creditString = rs.getString("credit_note");
			if (advanceString != null && advanceString.length() > 0) {
				if (invoiceString != null && invoiceString.length() > 0 )
					invoiceString += "," + advanceString;
				else
					invoiceString = advanceString;
			}
				
			if (creditString != null && creditString.length() > 0) {
				if (invoiceString != null && invoiceString.length() > 0 )
					invoiceString += "," + creditString;
				else
					invoiceString = creditString;
			}
			List<NameVo> invoiceList = new ArrayList<>();
			if (invoiceString != null && invoiceString.length() > 0) {
				String[] invoiceArray = invoiceString.split(",");
				for (String invoice : invoiceArray) {
					invoiceList.add(new NameVo(invoice));
				}
			}
			listVo.setInvoices(invoiceList);

			totalList.add(listVo);

		
		}
		return totalList;
	}

	public PaymentNonCoreVo updatePayment(PaymentNonCoreVo paymentVo) throws ApplicationException, SQLException {
		logger.info("Entry into method: updatePayment");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection connection = getAccountsPayable();
		connection.setAutoCommit(false);
		try {
			String paymentRefNo = formatPaymentRefNo(paymentVo.getPaymentRefNo(), paymentVo.getOrganizationId());
			if (!paymentRefNo.equals(getPaymentById(paymentVo.getId()).getPaymentRefNo())) {
				boolean isPaymentExist = checkPaymentExist(paymentRefNo, paymentVo.getOrganizationId(), connection);
				if (isPaymentExist) {
					throw new ApplicationException("Payment already exists for the organization");
				}
			}
			List<PaymentNonCoreBaseVo> paymentAfterRemoval = null;
			if (paymentVo.getItemsToRemove() != null) {
				paymentAfterRemoval = new ArrayList<PaymentNonCoreBaseVo>();
			}
			if (paymentVo.getPayments() != null) {
				for (PaymentNonCoreBaseVo paymentChild : paymentVo.getPayments()) {
					paymentChild.setPaymentRefId(paymentVo.getId());
					if (paymentVo.getItemsToRemove() != null) {
						if (!paymentVo.getItemsToRemove().contains(paymentChild.getId())) {
							paymentAfterRemoval.add(paymentChild);
						}
					}
				}
				if (paymentAfterRemoval != null)
					paymentVo.setPayments(paymentAfterRemoval);

				if (paymentVo.getItemsToRemove() != null) {
					for (Integer id : paymentVo.getItemsToRemove()) {// IF deleted
						deletePaymentDetails(id, connection, getPaymentTypeById(paymentVo.getPaymentType()));
					}

				}
			}

			List<CreditDetailsVo> creditDetails = null;
			if (paymentVo.getItemsToRemoveCredit() != null)
				creditDetails = new ArrayList<CreditDetailsVo>();
			if (paymentVo.getCreditList() != null) {
				for (CreditDetailsVo credit : paymentVo.getCreditList()) {
					if (paymentVo.getItemsToRemoveCredit() != null) {
						if (!paymentVo.getItemsToRemoveCredit().contains(credit.getId())) {
							creditDetails.add(credit);
						}
					}
				}
				if (creditDetails != null)
					paymentVo.setCreditList(creditDetails);

				if (paymentVo.getItemsToRemoveCredit() != null) {
					for (Integer id : paymentVo.getItemsToRemoveCredit()) {
						deleteCreditDetail(id, connection);
					}
				}
			}

			String sql = PaymentNonCoreConstants.UPDATE_NON_CORE_PAYMENT;
			preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, paymentVo.getPaidVia());
			preparedStatement.setInt(2, paymentVo.getPaymentType());
			preparedStatement.setString(3,
					formatPaymentRefNo(paymentVo.getPaymentRefNo(), paymentVo.getOrganizationId()));
			String date = paymentVo.getPaymentDate() != null
					? DateConverter.getInstance().correctDatePickerDateToString(paymentVo.getPaymentDate())
					: null;
//			DateConverter dc = DateConverter.getInstance();
//			Connection con1 = getUserMgmConnection();
//			String dateFormat = organizationDao.getDefaultDateForOrganization(paymentVo.getOrganizationId(), con1);
//			closeResources(null, null, con1);
//			if (date != null && date.length() > 0 && date.contains("T") && date.contains("Z")) {
//				date = dc.correctDatePickerDateToStringNonCorePaymentsCreate(date);
//			} else if (date != null && date.length() > 0 && dateFormat != null) {
//				date = dc.convertDateToGivenFormat(dc.convertStringToDate(date, dateFormat), "yyyy-MM-dd");
//				;
//			}

			preparedStatement.setDate(4, date != null && !date.isEmpty() ? Date.valueOf(date) : null);
			preparedStatement.setInt(5, paymentVo.getVendor());
			preparedStatement.setInt(6, paymentVo.getVendorAccountId());
			preparedStatement.setString(7, paymentVo.getVendorAccountName());
			preparedStatement.setInt(8, paymentVo.getCurrency());
			preparedStatement.setString(9, paymentVo.getNotes());
			preparedStatement.setString(10, paymentVo.getAdjustedAmount());
			preparedStatement.setString(11, paymentVo.getTotalAmount());
			preparedStatement.setString(12, paymentVo.getPoRefNo());
			preparedStatement.setInt(13, paymentVo.getContactAccountId());
			preparedStatement.setString(14, paymentVo.getContactAccountName());
			preparedStatement.setInt(15, paymentVo.getContactId());
			preparedStatement.setString(16, paymentVo.getAmountPaid());
			preparedStatement.setString(17, paymentVo.getDifferenceAmount());
			preparedStatement.setInt(18, paymentVo.getPayType());
			preparedStatement.setString(19, paymentVo.getPayPeriod());
			preparedStatement.setString(20, paymentVo.getPayRunRefNo());
			preparedStatement.setInt(21, paymentVo.getOrganizationId());
			preparedStatement.setBoolean(22, paymentVo.isSuperAdmin());
			preparedStatement.setString(23, paymentVo.getStatus());
			preparedStatement.setString(24, paymentVo.getPaidTo());
			String newJsonData = null;
			ObjectMapper mapper = new ObjectMapper();
			if (paymentVo.getCustomTableList() != null) {
				List<PaymentNonCoreCustomTableVo> customTableList = paymentVo.getCustomTableList();
				logger.info(customTableList.size());
				newJsonData = mapper.writeValueAsString(customTableList);
				logger.info(newJsonData);
			}
			preparedStatement.setString(25, newJsonData);
			preparedStatement.setString(26, paymentVo.getUserId());
			preparedStatement.setString(27, paymentVo.getRoleName());
			preparedStatement.setTimestamp(28, new Timestamp(System.currentTimeMillis()));

			preparedStatement.setInt(29, paymentVo.getPaymentMode());
			preparedStatement.setString(30, paymentVo.getContactType());
			preparedStatement.setInt(31, paymentVo.getStatutoryBody());
			preparedStatement.setInt(32, paymentVo.getCustomerName());
			preparedStatement.setInt(33, paymentVo.getEmployeeName());
			preparedStatement.setInt(34, paymentVo.getPaidFor());
			preparedStatement.setString(35, paymentVo.getCurrencyCode());
			preparedStatement.setString(36, paymentVo.getExchangeRate());
			preparedStatement.setInt(37, paymentVo.getBillRef());
			preparedStatement.setInt(38, paymentVo.getReferenceId());
			preparedStatement.setInt(39, paymentVo.getInvoiceId());
			preparedStatement.setString(40, paymentVo.getReferenceType());
			preparedStatement.setBoolean(41, paymentVo.isBulk());
			preparedStatement.setString(42, paymentVo.getPaidViaName());
			preparedStatement.setString(43, paymentVo.getContactName());
			preparedStatement.setString(44, paymentVo.getInvRefName());
			preparedStatement.setString(45, paymentVo.getBaseCurrencyCode());
			String glData = paymentVo.getGeneralLedgerData() != null
					? mapper.writeValueAsString(paymentVo.getGeneralLedgerData())
					: null;
			logger.info("glData>> " + glData);
			preparedStatement.setString(46, glData);
			if (paymentVo.getDisplayDueAmount() != null && paymentVo.getDisplayDueAmount().length() > 0)
				preparedStatement.setDouble(47, Double.parseDouble(paymentVo.getDisplayDueAmount()));
			else
				preparedStatement.setDouble(47, 0.0);
			preparedStatement.setString(48, paymentVo.getBankReferenceNumber());
			preparedStatement.setInt(49, paymentVo.getId());

			int rowAffected = preparedStatement.executeUpdate();

			if (rowAffected == 1) {

				// Update Child Objects
				if (paymentVo
						.getPayments() != null/* && paymentVo.getPayments().size() != PaymentNonCoreConstants.ZERO */) {
					updateChildTables(paymentVo);
				}

				if (getPaymentTypeById(paymentVo.getPaymentType()).equals(PaymentNonCoreConstants.VENDOR_ADVANCE)) {
					updateVendorAdvance(paymentVo, connection);
				}

				if (paymentVo.getAttachments() != null && paymentVo.getAttachments().size() > 0) {
					attachmentsDao.createAttachments(paymentVo.getOrganizationId(), paymentVo.getUserId(),
							paymentVo.getAttachments(), AttachmentsConstants.MODULE_TYPE_AP_PAYMENTS, paymentVo.getId(),
							paymentVo.getRoleName());
				}

				if (paymentVo.getAttachmentsToRemove() != null && paymentVo.getAttachmentsToRemove().size() > 0) {
					for (Integer attachmentId : paymentVo.getAttachmentsToRemove()) {
						attachmentsDao.changeStatusForSingleAttachment(attachmentId, CommonConstants.STATUS_AS_DELETE,
								paymentVo.getUserId(), paymentVo.getRoleName());
					}
				}

			}

			connection.commit();
		} catch (Exception e) {
			logger.error("Error in updatePayment", e);
			connection.rollback();
			logger.info("Before throw in dao :: " + e.getMessage());
			throw new ApplicationException(e.getMessage());
		} finally {
			closeResources(rs, preparedStatement, connection);
		}
		return paymentVo;
	}

	private void updateVendorAdvance(PaymentNonCoreVo paymentVo, Connection con) throws ApplicationException {

		logger.info("Entry into method: updateVendorAdvance");
		PreparedStatement preparedStatement = null;
		try {
			String query = PaymentNonCoreConstants.UPDATE_VENDOR_ADVANCE;
			logger.info(query);
			preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			preparedStatement.setString(1, paymentVo.getPaymentRefNo());
			preparedStatement.setInt(2, paymentVo.getVendor());
			preparedStatement.setInt(3, paymentVo.getId());

			preparedStatement.executeUpdate();

		} catch (Exception e) {
			logger.error("Error during createVendorAdvance ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatement, null);
		}

	}

	private void deleteCreditDetail(Integer id, Connection connection) throws ApplicationException {

		logger.info("Entry into method: deleteCreditDetail");
		PreparedStatement preparedStatement = null;

		try {
			String sql = PaymentNonCoreConstants.DELETE_CREDIT_DETAIL;

			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			logger.error("Error in deleteCreditDetail", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatement, null);
		}

	}

	private void updateChildTables(PaymentNonCoreVo paymentVo) throws ApplicationException {

		for (PaymentNonCoreBaseVo paymentChild : paymentVo.getPayments()) {
			paymentChild.setPaymentRefId(paymentVo.getId());
		}

		Connection con = getAccountsPayable();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			switch (getPaymentTypeById(paymentVo.getPaymentType())) {
			case PaymentNonCoreConstants.BILL_PAYMENTS:
				updateBillDetails(paymentVo, con);
				break;
			case PaymentNonCoreConstants.GST_PAYMENTS:
				updateGstDetails(paymentVo, con);
				break;
			case PaymentNonCoreConstants.TDS_PAYMENTS:
				updateTdsDetails(paymentVo, con);
				break;
			case PaymentNonCoreConstants.CUSTOMER_REFUNDS:
				updateCustomerRefunds(paymentVo, con);
				break;
			case PaymentNonCoreConstants.EMPLOYEE_PAYMENTS:
				updateEmployeeDetails(paymentVo, con);
				break;
			}
		} catch (Exception e) {
			logger.error("Error during updateChildTables ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

	}

	private void updateEmployeeDetails(PaymentNonCoreVo paymentVo, Connection con) throws ApplicationException {

		logger.info("Entry into method: updateEmployeeDetails");
		List<PaymentNonCoreBaseVo> payments = paymentVo.getPayments();
		PreparedStatement preparedStatement = null;
		try {
			String query = PaymentNonCoreConstants.UPDATE_EMPLOYEE_DETAILS;
			logger.info(query);
			preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			for (PaymentNonCoreBaseVo payment : payments) {
				if (payment.getId() == null) {
					PaymentNonCoreVo newPaymentDetail = new PaymentNonCoreVo();
					newPaymentDetail.setId(paymentVo.getId());
					List<PaymentNonCoreBaseVo> paymentList = new ArrayList<PaymentNonCoreBaseVo>();
					payment.setStatus(CommonConstants.STATUS_AS_ACTIVE);
					paymentList.add(payment);
					newPaymentDetail.setPayments(paymentList);
					createEmployeeDetails(newPaymentDetail, con);
				} else {
					preparedStatement.setInt(1, paymentVo.getId());
					preparedStatement.setString(2, payment.getStatus());
					preparedStatement.setString(3, payment.getEmpName());
					preparedStatement.setString(4, payment.getNetPay());

					String data = "{\"data\":";
					data += "{\n" + "\"payable\":\"" + payment.getPayable() + "\"," + "\"advance\":\""
							+ payment.getAdvance() + "\"," + "\"others3\":\"" + payment.getOthers3() + "\","
							+ "\"others2\":\"" + payment.getOthers2() + "\"," + "\"others1\":\"" + payment.getOthers1()
							+ "\"" + "}" + "}";

					preparedStatement.setString(5, data);
					preparedStatement.setInt(6, payment.getPayRun());
					if (payment.getDueAmount() != null && payment.getDueAmount().length() > 0)
						preparedStatement.setDouble(7, Double.parseDouble(payment.getDueAmount()));
					else
						preparedStatement.setDouble(7, 0.0);
					preparedStatement.setInt(8, payment.getId());

					preparedStatement.executeUpdate();
				}
			}

		} catch (Exception e) {
			logger.error("Error during updateBillDetails ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatement, null);
		}

	}

	private void updateCustomerRefunds(PaymentNonCoreVo paymentVo, Connection con) throws ApplicationException {

		logger.info("Entry into method: updateCustomerRefunds");
		List<PaymentNonCoreBaseVo> payments = paymentVo.getPayments();
		PreparedStatement preparedStatement = null;
		try {
			String query = PaymentNonCoreConstants.UPDATE_CUSTOMER_REFUND;
			logger.info(query);
			preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			for (PaymentNonCoreBaseVo payment : payments) {
				if (payment.getId() == null) {
					PaymentNonCoreVo newPaymentDetail = new PaymentNonCoreVo();
					newPaymentDetail.setId(paymentVo.getId());
					newPaymentDetail.setBulk(paymentVo.isBulk());
					newPaymentDetail.setAmountPaid(paymentVo.getAmountPaid());
					List<PaymentNonCoreBaseVo> paymentList = new ArrayList<PaymentNonCoreBaseVo>();
					payment.setStatus(CommonConstants.STATUS_AS_ACTIVE);
					paymentList.add(payment);
					newPaymentDetail.setPayments(paymentList);
					createCustomerRefunds(newPaymentDetail, con);
				} else {
					preparedStatement.setInt(1, paymentVo.getId());
					preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
					preparedStatement.setInt(3, payment.getInvoiceId());
					String refundAmount = payment.getInvoiceAmount();
					if (!paymentVo.isBulk()) {
						refundAmount = paymentVo.getAmountPaid();
					}
					preparedStatement.setString(4, refundAmount);
					preparedStatement.setInt(5, payment.getReferenceId());
					preparedStatement.setString(6, payment.getReferenceType());
					preparedStatement.setString(7, payment.getInvRefName());
					preparedStatement.setInt(8, payment.getId());
//					preparedStatement.setDouble(9, Double.parseDouble(payment.getDueAmount()));

					preparedStatement.executeUpdate();
				}
			}

		} catch (Exception e) {
			logger.error("Error during updateCustomerRefunds ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatement, null);
		}

	}

	private void updateTdsDetails(PaymentNonCoreVo paymentVo, Connection con) throws ApplicationException {

		logger.info("Entry into method: updateTdsDetails");
		List<PaymentNonCoreBaseVo> payments = paymentVo.getPayments();
		PreparedStatement preparedStatement = null;
		try {
			String query = PaymentNonCoreConstants.UPDATE_TDS_DETAILS;
			logger.info(query);
			preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			for (PaymentNonCoreBaseVo payment : payments) {
				if (payment.getId() == null) {
					PaymentNonCoreVo newPaymentDetail = new PaymentNonCoreVo();
					newPaymentDetail.setId(paymentVo.getId());
					List<PaymentNonCoreBaseVo> paymentList = new ArrayList<PaymentNonCoreBaseVo>();
					payment.setStatus(CommonConstants.STATUS_AS_ACTIVE);
					paymentList.add(payment);
					newPaymentDetail.setPayments(paymentList);
					createTdsDetails(newPaymentDetail, con);
				} else {
					preparedStatement.setInt(1, paymentVo.getId());
					preparedStatement.setString(2, payment.getStatus());
					preparedStatement.setString(3, payment.getTdsSection());
					preparedStatement.setString(4, payment.getType());
					preparedStatement.setString(5, payment.getPaidAmt());

					String data = "{\"data\":";
					data += "{\n" + "\"tax\":\"" + payment.getTax() + "\"," + "\"interest\":\"" + payment.getInterest()
							+ "\"," + "\"penalty\":\"" + payment.getPenalty() + "\"," + "\"others1\":\""
							+ payment.getOthers1() + "\"," + "\"others2\":\"" + payment.getOthers2() + "\"" + "}" + "}";

					preparedStatement.setString(6, data);
					preparedStatement.setInt(7, payment.getId());

					preparedStatement.executeUpdate();
				}
			}

		} catch (Exception e) {
			logger.error("Error during updateTdsDetails ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatement, null);
		}

	}

	private void updateGstDetails(PaymentNonCoreVo paymentVo, Connection con) throws ApplicationException {

		logger.info("Entry into method: updateGstDetails");
		List<PaymentNonCoreBaseVo> payments = paymentVo.getPayments();
		PreparedStatement preparedStatement = null;
		try {
			String query = PaymentNonCoreConstants.UPDATE_GST_DETAILS;
			logger.info(query);
			preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			for (PaymentNonCoreBaseVo payment : payments) {
				if (payment.getId() == null) {
					PaymentNonCoreVo newPaymentDetail = new PaymentNonCoreVo();
					newPaymentDetail.setId(paymentVo.getId());
					List<PaymentNonCoreBaseVo> paymentList = new ArrayList<PaymentNonCoreBaseVo>();
					payment.setStatus(CommonConstants.STATUS_AS_ACTIVE);
					paymentList.add(payment);
					newPaymentDetail.setPayments(paymentList);
					createGstDetails(newPaymentDetail, con);
				} else {
					preparedStatement.setInt(1, paymentVo.getId());
					preparedStatement.setString(2, payment.getStatus());
					preparedStatement.setString(3, payment.getGstType());
					preparedStatement.setString(4, payment.getPaidAmt());

					String data = "{\"data\":";
					data += "{\n" + "\"tax\":\"" + payment.getTax() + "\"," + "\"interest\":\"" + payment.getInterest()
							+ "\"," + "\"penalty\":\"" + payment.getPenalty() + "\"," + "\"others1\":\""
							+ payment.getOthers1() + "\"," + "\"others2\":\"" + payment.getOthers2() + "\"" + "}" + "}";

					preparedStatement.setString(5, data);
					preparedStatement.setInt(6, payment.getId());

					preparedStatement.executeUpdate();
				}
			}

		} catch (Exception e) {
			logger.error("Error during updateGstDetails ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatement, null);
		}

	}

	private void updateBillDetails(PaymentNonCoreVo paymentVo, Connection con) throws ApplicationException {

		logger.info("Entry into method: updateBillDetails");
		List<PaymentNonCoreBaseVo> payments = paymentVo.getPayments();
		PreparedStatement preparedStatement = null;
		try {
			String query = PaymentNonCoreConstants.UPDATE_BILL_DETAILS;
			logger.info(query);
			preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			for (PaymentNonCoreBaseVo payment : payments) {
				if (payment.getId() == null) {
					PaymentNonCoreVo newPaymentDetail = new PaymentNonCoreVo();
					newPaymentDetail.setId(paymentVo.getId());
					List<PaymentNonCoreBaseVo> paymentList = new ArrayList<PaymentNonCoreBaseVo>();
					payment.setStatus(CommonConstants.STATUS_AS_ACTIVE);
					paymentList.add(payment);
					newPaymentDetail.setPayments(paymentList);
					createBillDetails(newPaymentDetail, con);
				} else {
//					String oldAmount = fetchAmountPaidByBillDetailId(payment.getId(), con);
					preparedStatement.setInt(1, paymentVo.getId());
					preparedStatement.setString(2, CommonConstants.STATUS_AS_ACTIVE);
					preparedStatement.setString(3, payment.getBillRef());
					preparedStatement.setString(4, payment.getBillAmount());
					preparedStatement.setString(5, payment.getTotalAmount());

					String data = "{\"data\":";
					data += "{\n" + "\"dueAmount\":\"" + payment.getDueAmount() + "\"," + "\"dueDate\":\""
							+ payment.getDueDate() + "\"," + "\"bankFees\":\"" + payment.getBankCharges() + "\","
							+ "\"tdsDeductions\":\"" + payment.getTdsDeducted() + "\"," + "\"others2\":\""
							+ payment.getOthers2() + "\"," + "\"others1\":\"" + payment.getOthers1() + "\"" + "}" + "}";

					preparedStatement.setString(6, data);
					preparedStatement.setString(7, payment.getType());
					preparedStatement.setInt(8, payment.getVendorId());
					preparedStatement.setInt(9, payment.getId());

					preparedStatement.executeUpdate();

				}
			}

		} catch (Exception e) {
			logger.error("Error during updateBillDetails ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatement, null);
		}

	}

//	private String fetchAmountPaidByBillDetailId(Integer id, Connection con) throws ApplicationException {
//
//		logger.info("Entry into method: fetchAmountPaidByBillDetailId");
//		PreparedStatement preparedStatement = null;
//		ResultSet rs = null;
//		String amount = null;
//		try {
//			String query = PaymentNonCoreConstants.FETCH_BILL_DETAILS_BY_ID;
//			logger.info(query);
//			preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
//			preparedStatement.setInt(1, id);
//			rs = preparedStatement.executeQuery();
//			while (rs.next()) {
//				amount = rs.getString(1);
//			}
//
//		} catch (Exception e) {
//			logger.error("Error during fetchCreditDetails ", e);
//			throw new ApplicationException(e);
//		} finally {
//			closeResources(rs, preparedStatement, null);
//		}
//
//		return amount;
//
//	}

	private PaymentNonCoreVo deletePaymentDetails(Integer id, Connection connection, String paymentType)
			throws ApplicationException {

		logger.info("Entry into method: deletePaymentDetails");
		PreparedStatement preparedStatement = null;

		PaymentNonCoreVo paymentVo = new PaymentNonCoreVo();
		try {
			String sql = "";
			switch (paymentType) {
			case PaymentNonCoreConstants.BILL_PAYMENTS:
				sql = PaymentNonCoreConstants.DELETE_BILL_DETAIL_BY_ID;
				break;
			case PaymentNonCoreConstants.GST_PAYMENTS:
				sql = PaymentNonCoreConstants.DELETE_GST_DETAIL_BY_ID;
				break;
			case PaymentNonCoreConstants.TDS_PAYMENTS:
				sql = PaymentNonCoreConstants.DELETE_TDS_DETAIL_BY_ID;
				break;
			case PaymentNonCoreConstants.CUSTOMER_REFUNDS:
				sql = PaymentNonCoreConstants.DELETE_CR_DETAIL_BY_ID;
				break;
			case PaymentNonCoreConstants.EMPLOYEE_PAYMENTS:
				sql = PaymentNonCoreConstants.DELETE_EMPLOYEE_DETAIL_BY_ID;
				break;
			}
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			paymentVo.setId(id);
			preparedStatement.executeUpdate();

		} catch (Exception e) {
			logger.error("Error in deletePaymentDetails", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(null, preparedStatement, null);
		}
		return paymentVo;

	}

	public PaymentNonCoreVo getPaymentById(int id) throws ApplicationException {
		logger.info("Entry into method: getPaymentById");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		PaymentNonCoreVo paymentVo = new PaymentNonCoreVo();
		paymentVo.setId(id);
		try {
			con = getAccountsPayable();
			String query = PaymentNonCoreConstants.GET_PAYMENT_BY_ID;
			logger.info(query);
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				paymentVo.setPaidVia(rs.getInt(1));
				paymentVo.setPaymentType(rs.getInt(2));
				paymentVo.setPaymentRefNo(rs.getString(3));
				Date paymentDate = rs.getDate(4);
				paymentVo.setPaymentDate(
						paymentDate != null ? DateConverter.getInstance().getDatePickerDateFormat(paymentDate) : null);
				paymentVo.setVendor(rs.getInt(5));
				paymentVo.setVendorAccountId(rs.getInt(6));
				paymentVo.setVendorAccountName(rs.getString(7));
				paymentVo.setCurrency(rs.getInt(8));
				paymentVo.setNotes(rs.getString(9));
				paymentVo.setAdjustedAmount(rs.getString(10));
				paymentVo.setTotalAmount(rs.getString(11));
				paymentVo.setPoRefNo(rs.getString(12));
				paymentVo.setContactAccountId(rs.getInt(13));
				paymentVo.setContactAccountName(rs.getString(14));
				paymentVo.setContactId(rs.getInt(15));
				paymentVo.setAmountPaid(rs.getString(16));
				paymentVo.setDifferenceAmount(rs.getString(17));
				paymentVo.setPayType(rs.getInt(18));
				paymentVo.setPayPeriod(rs.getString(19));
				paymentVo.setPayRunRefNo(rs.getString(20));
				paymentVo.setUserId(rs.getString(21));
				paymentVo.setOrganizationId(rs.getInt(22));
				paymentVo.setRoleName(rs.getString(23));
				paymentVo.setSuperAdmin(rs.getBoolean(24));
				paymentVo.setStatus(rs.getString(25));
				paymentVo.setPaidTo(rs.getString(26));

				String customTableList = rs.getString(27);

				List<PaymentNonCoreCustomTableVo> customTables = new ArrayList<PaymentNonCoreCustomTableVo>();
				if (customTableList != null && customTableList.length() > 4) {
					JSONParser parser = new JSONParser();
					JSONArray columnsArray = (JSONArray) parser.parse(customTableList);

					for (Object obj : columnsArray) {
						JSONObject columnsObj = (JSONObject) obj;

						PaymentNonCoreCustomTableVo customTable = new PaymentNonCoreCustomTableVo();
						customTable.setLedgerName(
								columnsObj.get("ledgerName") != null ? columnsObj.get("ledgerName").toString() : null);
						customTable.setLedgerId(
								columnsObj.get("ledgerId") != null ? columnsObj.get("ledgerId").toString() : null);
						customTable
								.setcName(columnsObj.get("cName") != null ? columnsObj.get("cName").toString() : null);
						customTable.setColumnShow(
								columnsObj.get("columnShow") != null ? (boolean) columnsObj.get("columnShow") : false);
						customTable.setColName(
								columnsObj.get("colName") != null ? columnsObj.get("colName").toString() : null);

						customTables.add(customTable);
					}
					paymentVo.setCustomTableList(customTables);
				}

				paymentVo.setPaymentMode(rs.getInt(28));
				paymentVo.setContactType(rs.getString(29));
				paymentVo.setStatutoryBody(rs.getInt(30));
				paymentVo.setCustomerName(rs.getInt(31));
				paymentVo.setEmployeeName(rs.getInt(32));
				paymentVo.setPaidFor(rs.getInt(33));
				paymentVo.setCurrencyCode(rs.getString(34));
				paymentVo.setExchangeRate(rs.getString(35));
				paymentVo.setBillRef(rs.getInt(36));
				paymentVo.setReferenceId(rs.getInt(37));
				paymentVo.setInvoiceId(rs.getInt(38));
				paymentVo.setReferenceType(rs.getString(39));
				paymentVo.setBulk(rs.getBoolean(40));
				paymentVo.setPaidViaName(rs.getString(41));
				paymentVo.setContactName(rs.getString(42));
				paymentVo.setInvRefName(rs.getString(43));
				paymentVo.setBaseCurrencyCode(rs.getString(44));
				ObjectMapper mapper = new ObjectMapper();
				String json = rs.getString(45);
				if (json != null) {
					GeneralLedgerVo gldata = mapper.readValue(json, GeneralLedgerVo.class);
					logger.info("Json map " + gldata);
					paymentVo.setGeneralLedgerData(gldata);
					paymentVo.setCreateTs(rs.getDate(46));
				}

				double displayDueAmount = rs.getDouble(47);
				if (displayDueAmount > 0) {
					paymentVo.setDisplayDueAmount(String.valueOf(displayDueAmount));
				} else {
					paymentVo.setDisplayDueAmount(null);
				}

				paymentVo = getPaymentDetailsByPaymentId(paymentVo);

				if (getPaymentTypeById(paymentVo.getPaymentType())
						.equalsIgnoreCase(PaymentNonCoreConstants.BILL_PAYMENTS)) {
					paymentVo = fetchCreditDetails(paymentVo);
				}

				List<UploadFileVo> uploadFileVos = new ArrayList<UploadFileVo>();
				for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(paymentVo.getId(),
						AttachmentsConstants.MODULE_TYPE_AP_PAYMENTS)) {
					UploadFileVo uploadFileVo = new UploadFileVo();
					uploadFileVo.setId(attachments.getId());
					uploadFileVo.setName(attachments.getFileName());
					uploadFileVo.setSize(attachments.getSize());
					uploadFileVos.add(uploadFileVo);
				}
				paymentVo.setAttachments(uploadFileVos);
				paymentVo.setBankReferenceNumber(rs.getString(48));
			}

		} catch (Exception e) {
			logger.error("Error during getAllReceiptsOfAnOrganization", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

		return paymentVo;

	}

	private PaymentNonCoreVo fetchCreditDetails(PaymentNonCoreVo paymentVo) throws ApplicationException {

		logger.info("Entry into method: fetchCreditDetails");
		List<CreditDetailsVo> creditDetails = new ArrayList<CreditDetailsVo>();
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsPayable();
			String query = PaymentNonCoreConstants.FETCH_CREDIT_DETAILS_ON_PAYMENT_REF_ID;
			logger.info(query);
			preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, paymentVo.getId());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				CreditDetailsVo credit = new CreditDetailsVo();
				credit.setId(rs.getInt(1));
				credit.setStatus(rs.getString(2));
				credit.setReference(rs.getString(3));
				credit.setOriginalAmt(rs.getString(4));
				credit.setAvailableAmt(rs.getString(5));
				credit.setAdjustmentAmount(rs.getString(6));
				credit.setDeleted(rs.getBoolean(7));
				credit.setCreatedNew(rs.getBoolean(8));
				paymentVo.setCreditAccountId(rs.getInt(9));

				creditDetails.add(credit);
			}

		} catch (Exception e) {
			logger.error("Error during fetchCreditDetails ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

		paymentVo.setCreditList(creditDetails);
		return paymentVo;

	}

	private PaymentNonCoreVo getPaymentDetailsByPaymentId(PaymentNonCoreVo paymentVo) throws ApplicationException {

		Connection con = getAccountsPayable();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			switch (getPaymentTypeById(paymentVo.getPaymentType())) {
			case PaymentNonCoreConstants.BILL_PAYMENTS:
				paymentVo = fetchBillDetails(paymentVo, con);
				break;
			case PaymentNonCoreConstants.GST_PAYMENTS:
				paymentVo = fetchGstDetails(paymentVo, con);
				break;
			case PaymentNonCoreConstants.TDS_PAYMENTS:
				paymentVo = fetchTdsDetails(paymentVo, con);
				break;
			case PaymentNonCoreConstants.CUSTOMER_REFUNDS:
				paymentVo = fetchCustomerRefunds(paymentVo, con);
				break;
			case PaymentNonCoreConstants.EMPLOYEE_PAYMENTS:
				paymentVo = fetchEmployeeDetails(paymentVo, con);
				break;
			}
		} catch (Exception e) {
			logger.error("Error during getPaymentDetailsByPaymentId ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

		return paymentVo;

	}

	private PaymentNonCoreVo fetchEmployeeDetails(PaymentNonCoreVo paymentVo, Connection con)
			throws ApplicationException {

		logger.info("Entry into method: fetchEmployeeDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<PaymentNonCoreBaseVo> payments = new ArrayList<PaymentNonCoreBaseVo>();
		try {
			String query = PaymentNonCoreConstants.FETCH_EMPLOYEE_DETAILS_BY_PAYMENT_ID;
			logger.info(query);
			preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, paymentVo.getId());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PaymentNonCoreBaseVo payment = new PaymentNonCoreBaseVo();
				payment.setId(rs.getInt(1));
				payment.setStatus(rs.getString(2));
				payment.setEmpName(rs.getString(3));
				payment.setNetPay(rs.getString(4));

				String data = rs.getString(5);
				if (data != null) {
					JSONParser parser = new JSONParser();
					JSONObject dataJson = (JSONObject) parser.parse(data);
					JSONObject columnsObj = (JSONObject) dataJson.get("data");

					payment.setPayable(columnsObj.get("payable") != null
							&& !columnsObj.get("payable").toString().equalsIgnoreCase(CommonConstants.NULL)
									? columnsObj.get("payable").toString()
									: null);
					payment.setAdvance(columnsObj.get("advance") != null
							&& !columnsObj.get("advance").toString().equalsIgnoreCase(CommonConstants.NULL)
									? columnsObj.get("advance").toString()
									: null);
					payment.setOthers3(columnsObj.get("others3") != null
							&& !columnsObj.get("others3").toString().equalsIgnoreCase(CommonConstants.NULL)
									? columnsObj.get("others3").toString()
									: null);
					payment.setOthers1(columnsObj.get("others1") != null
							&& !columnsObj.get("others1").toString().equalsIgnoreCase(CommonConstants.NULL)
									? columnsObj.get("others1").toString()
									: null);
					payment.setOthers2(columnsObj.get("others2") != null
							&& !columnsObj.get("others2").toString().equalsIgnoreCase(CommonConstants.NULL)
									? columnsObj.get("others2").toString()
									: null);

				}
				payment.setPayRun(rs.getInt(6));
				payment.setDueAmount(String.valueOf(rs.getDouble(7)));
				payments.add(payment);
			}

		} catch (Exception e) {
			logger.error("Error during fetchEmployeeDetails ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}

		paymentVo.setPayments(payments);
		return paymentVo;

	}

	public PaymentNonCoreVo fetchCustomerRefunds(PaymentNonCoreVo paymentVo, Connection con)
			throws ApplicationException {

		logger.info("Entry into method: fetchCustomerRefunds");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<PaymentNonCoreBaseVo> payments = new ArrayList<PaymentNonCoreBaseVo>();
		try {
			String query = PaymentNonCoreConstants.FETCH_CUSTOMER_REFUND_DETAILS_BY_PAYMENT_ID;
			logger.info(query);
			preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, paymentVo.getId());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PaymentNonCoreBaseVo payment = new PaymentNonCoreBaseVo();
				payment.setId(rs.getInt(1));
				payment.setPaymentRefId(rs.getInt(2));
				payment.setStatus(rs.getString(3));
				payment.setInvoiceId(rs.getInt(4));
				payment.setInvoiceAmount(rs.getString(5));
				payment.setReferenceId(rs.getInt(6));
				payment.setReferenceType(rs.getString(7));
				payment.setInvRefName(rs.getString(8));
//				payment.setDueAmount(Double.toString(rs.getDouble(9)));

				payments.add(payment);
			}

		} catch (Exception e) {
			logger.error("Error during fetchCustomerRefunds ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}

		paymentVo.setPayments(payments);
		return paymentVo;

	}

	public List<PaymentNonCoreBaseVo> fetchCustomerRefundsByPaymentIdAndReference(Integer paymentId,
			String referenceType, Connection con) throws ApplicationException {

		logger.info("Entry into method: fetchCustomerRefunds");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<PaymentNonCoreBaseVo> payments = new ArrayList<PaymentNonCoreBaseVo>();
		try {
			String query = PaymentNonCoreConstants.FETCH_CR_BY_PAYMENT_ID_AND_REFEENCE_TYPE;
			logger.info(query);
			preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, paymentId);
			preparedStatement.setString(2, referenceType);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PaymentNonCoreBaseVo payment = new PaymentNonCoreBaseVo();
				payment.setId(rs.getInt(1));
				payment.setPaymentRefId(rs.getInt(2));
				payment.setStatus(rs.getString(3));
				payment.setInvoiceId(rs.getInt(4));
				payment.setInvoiceAmount(rs.getString(5));
				payment.setReferenceId(rs.getInt(6));
				payment.setReferenceType(rs.getString(7));
				payment.setInvRefName(rs.getString(8));

				payments.add(payment);
			}

		} catch (Exception e) {
			logger.error("Error during fetchCustomerRefunds ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}

		return payments;

	}

	private PaymentNonCoreVo fetchTdsDetails(PaymentNonCoreVo paymentVo, Connection con) throws ApplicationException {

		logger.info("Entry into method: fetchTdsDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<PaymentNonCoreBaseVo> payments = new ArrayList<PaymentNonCoreBaseVo>();
		try {
			String query = PaymentNonCoreConstants.FETCH_TDS_DETAILS_BY_PAYMENT_ID;
			logger.info(query);
			preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, paymentVo.getId());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PaymentNonCoreBaseVo payment = new PaymentNonCoreBaseVo();
				payment.setId(rs.getInt(1));
				payment.setPaymentRefId(rs.getInt(2));
				payment.setStatus(rs.getString(3));
				payment.setTdsSection(rs.getString(4));
				payment.setType(rs.getString(5));
				payment.setPaidAmt(rs.getString(6));

				String data = rs.getString(7);
				if (data != null) {
					JSONParser parser = new JSONParser();
					JSONObject dataJson = (JSONObject) parser.parse(data);
					JSONObject columnsObj = (JSONObject) dataJson.get("data");

					payment.setTax(columnsObj.get("tax") != null
							&& !columnsObj.get("tax").toString().equalsIgnoreCase(CommonConstants.NULL)
									? columnsObj.get("tax").toString()
									: null);
					payment.setInterest(columnsObj.get("interest") != null
							&& !columnsObj.get("interest").toString().equalsIgnoreCase(CommonConstants.NULL)
									? columnsObj.get("interest").toString()
									: null);
					payment.setPenalty(columnsObj.get("penalty") != null
							&& !columnsObj.get("penalty").toString().equalsIgnoreCase(CommonConstants.NULL)
									? columnsObj.get("penalty").toString()
									: null);
					payment.setOthers1(columnsObj.get("others1") != null
							&& !columnsObj.get("others1").toString().equalsIgnoreCase(CommonConstants.NULL)
									? columnsObj.get("others1").toString()
									: null);
					payment.setOthers2(columnsObj.get("others2") != null
							&& !columnsObj.get("others2").toString().equalsIgnoreCase(CommonConstants.NULL)
									? columnsObj.get("others2").toString()
									: null);
					paymentVo.setTax(columnsObj.get("taxLedgerId") != null
							&& !columnsObj.get("taxLedgerId").toString().equalsIgnoreCase(CommonConstants.NULL)
									? columnsObj.get("taxLedgerId").toString()
									: null);
					paymentVo.setInterest(columnsObj.get("interestLedgerId") != null
							&& !columnsObj.get("interestLedgerId").toString().equalsIgnoreCase(CommonConstants.NULL)
									? columnsObj.get("interestLedgerId").toString()
									: null);
					paymentVo.setPenalty(columnsObj.get("penaltyLedgerId") != null
							&& !columnsObj.get("penaltyLedgerId").toString().equalsIgnoreCase(CommonConstants.NULL)
									? columnsObj.get("penaltyLedgerId").toString()
									: null);
				}

				payments.add(payment);
			}

		} catch (Exception e) {
			logger.error("Error during fetchTdsDetails ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}

		paymentVo.setPayments(payments);
		return paymentVo;
	}

	private PaymentNonCoreVo fetchGstDetails(PaymentNonCoreVo paymentVo, Connection con) throws ApplicationException {

		logger.info("Entry into method: fetchGstDetails");
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<PaymentNonCoreBaseVo> payments = new ArrayList<PaymentNonCoreBaseVo>();
		try {
			String query = PaymentNonCoreConstants.FETCH_GST_DETAILS_BY_PAYMENT_ID;
			logger.info(query);
			preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, paymentVo.getId());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PaymentNonCoreBaseVo payment = new PaymentNonCoreBaseVo();
				payment.setId(rs.getInt(1));
				payment.setPaymentRefId(rs.getInt(2));
				payment.setStatus(rs.getString(3));
				payment.setGstType(rs.getString(4));
				payment.setPaidAmt(rs.getString(5));

				String data = rs.getString(6);
				if (data != null) {
					JSONParser parser = new JSONParser();
					JSONObject dataJson = (JSONObject) parser.parse(data);
					JSONObject columnsObj = (JSONObject) dataJson.get("data");

					payment.setTax(columnsObj.get("tax") != null
							&& !columnsObj.get("tax").toString().equalsIgnoreCase(CommonConstants.NULL)
									? columnsObj.get("tax").toString()
									: null);
					payment.setInterest(columnsObj.get("interest") != null
							&& !columnsObj.get("interest").toString().equalsIgnoreCase(CommonConstants.NULL)
									? columnsObj.get("interest").toString()
									: null);
					payment.setPenalty(columnsObj.get("penalty") != null
							&& !columnsObj.get("penalty").toString().equalsIgnoreCase(CommonConstants.NULL)
									? columnsObj.get("penalty").toString()
									: null);
					payment.setOthers1(columnsObj.get("others1") != null
							&& !columnsObj.get("others1").toString().equalsIgnoreCase(CommonConstants.NULL)
									? columnsObj.get("others1").toString()
									: null);
					payment.setOthers2(columnsObj.get("others2") != null
							&& !columnsObj.get("others2").toString().equalsIgnoreCase(CommonConstants.NULL)
									? columnsObj.get("others2").toString()
									: null);
					paymentVo.setTax(columnsObj.get("taxLedgerId") != null
							&& !columnsObj.get("taxLedgerId").toString().equalsIgnoreCase(CommonConstants.NULL)
									? columnsObj.get("taxLedgerId").toString()
									: null);
					paymentVo.setInterest(columnsObj.get("interestLedgerId") != null
							&& !columnsObj.get("interestLedgerId").toString().equalsIgnoreCase(CommonConstants.NULL)
									? columnsObj.get("interestLedgerId").toString()
									: null);
					paymentVo.setPenalty(columnsObj.get("penaltyLedgerId") != null
							&& !columnsObj.get("penaltyLedgerId").toString().equalsIgnoreCase(CommonConstants.NULL)
									? columnsObj.get("penaltyLedgerId").toString()
									: null);

				}

				payments.add(payment);
			}

		} catch (Exception e) {
			logger.error("Error during fetchGstDetails ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}

		paymentVo.setPayments(payments);
		return paymentVo;
	}

	public PaymentNonCoreVo fetchBillDetails(PaymentNonCoreVo paymentVo, Connection con) throws ApplicationException {

		logger.info("Entry into method: fetchBillDetails");
		List<PaymentNonCoreBaseVo> payments = new ArrayList<PaymentNonCoreBaseVo>();
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			String query = PaymentNonCoreConstants.FETCH_BILL_DETAILS_BY_PAYMENT_ID;
			logger.info(query);
			preparedStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, paymentVo.getId());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PaymentNonCoreBaseVo payment = new PaymentNonCoreBaseVo();
				payment.setId(rs.getInt(1));
				payment.setPaymentRefId(rs.getInt(2));
				payment.setStatus(rs.getString(3));
				payment.setBillRef(rs.getString(4));
				payment.setBillAmount(rs.getString(5));
				payment.setTotalAmount(rs.getString(6));

				String data = rs.getString(7);
				if (data != null) {
					JSONParser parser = new JSONParser();
					JSONObject dataJson = (JSONObject) parser.parse(data);
					JSONObject columnsObj = (JSONObject) dataJson.get("data");

					payment.setDueAmount(columnsObj.get("dueAmount") != null
							&& !columnsObj.get("dueAmount").toString().equalsIgnoreCase(CommonConstants.NULL)
									? columnsObj.get("dueAmount").toString()
									: null);
					payment.setDueDate(columnsObj.get("dueDate") != null
							&& !columnsObj.get("dueDate").toString().equalsIgnoreCase(CommonConstants.NULL)
									? columnsObj.get("dueDate").toString()
									: null);
					payment.setBankCharges(columnsObj.get("bankFees") != null
							&& !columnsObj.get("bankFees").toString().equalsIgnoreCase(CommonConstants.NULL)
									? columnsObj.get("bankFees").toString()
									: null);
					payment.setTdsDeducted(columnsObj.get("tdsDeductions") != null
							&& !columnsObj.get("tdsDeductions").toString().equalsIgnoreCase(CommonConstants.NULL)
									? columnsObj.get("tdsDeductions").toString()
									: null);
					payment.setOthers1(columnsObj.get("others1") != null
							&& !columnsObj.get("others1").toString().equalsIgnoreCase(CommonConstants.NULL)
									? columnsObj.get("others1").toString()
									: null);
					payment.setOthers2(columnsObj.get("others2") != null
							&& !columnsObj.get("others2").toString().equalsIgnoreCase(CommonConstants.NULL)
									? columnsObj.get("others2").toString()
									: null);
					paymentVo.setDueAmount(columnsObj.get("dueAmountLedgerId") != null
							&& !columnsObj.get("dueAmountLedgerId").toString().equalsIgnoreCase(CommonConstants.NULL)
									? columnsObj.get("dueAmountLedgerId").toString()
									: null);
					paymentVo.setBankFees(columnsObj.get("bankFeesLedgerId") != null
							&& !columnsObj.get("bankFeesLedgerId").toString().equalsIgnoreCase(CommonConstants.NULL)
									? columnsObj.get("bankFeesLedgerId").toString()
									: null);
					paymentVo.setTdsDeductions(columnsObj.get("tdsDeductionsLedgerId") != null && !columnsObj
							.get("tdsDeductionsLedgerId").toString().equalsIgnoreCase(CommonConstants.NULL)
									? columnsObj.get("tdsDeductionsLedgerId").toString()
									: null);
				}
				payment.setType(rs.getString(8));
				payment.setVendorId(rs.getInt(9));
				payments.add(payment);
			}

		} catch (Exception e) {
			logger.error("Error during fetchBillDetails ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, null);
		}

		paymentVo.setPayments(payments);
		return paymentVo;
	}

	public List<PaymentTypeVo> getPaymentTypes() throws SQLException, ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<PaymentTypeVo> paymentTypes = new ArrayList<PaymentTypeVo>();
		String query = PaymentNonCoreConstants.GET_PAYMENT_TYPES;
		try {
			con = getFinanceCommon();
			logger.info(query);
			preparedStatement = con.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				PaymentTypeVo paymentTypeVo = new PaymentTypeVo();
				paymentTypeVo.setId(rs.getInt(1));
				paymentTypeVo.setName(rs.getString(2));
				paymentTypeVo.setValue(rs.getInt(1));
				if(paymentTypeVo.getName().equals("Customer Refunds")|| paymentTypeVo.getName().equals("Employee Payments")){
					logger.info("Temporarily Removed for Customer Refunds and Employee Payments");
				}else{
					paymentTypes.add(paymentTypeVo);
				}

			}
		} catch (Exception e) {
			logger.error("Error during getPaymentTypes", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return paymentTypes;
	}

	public List<String> getPaymentRefNosByOrgId(int organizationId) throws ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<String> payRefNos = new ArrayList<String>();
		String query = PaymentNonCoreConstants.GET_PAYMENT_REF_NOS;
		try {
			con = getAccountsPayable();
			logger.info(query);
			preparedStatement = con.prepareStatement(query);

			preparedStatement.setInt(1, organizationId);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				payRefNos.add(rs.getString(1));
			}

		} catch (Exception e) {
			logger.error("Error during getPaymentRefNosByOrgId", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return payRefNos;
	}

	public String getPaymentTypeById(int id) throws SQLException, ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String paymentType = null;
		String query = PaymentNonCoreConstants.GET_PAYMENT_TYPE_BY_ID;
		try {
			con = getFinanceCommon();
			logger.info(query);
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				paymentType = rs.getString(1);
			}
		} catch (Exception e) {
			logger.error("Error during getPaymentTypes", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return paymentType;
	}

	public List<Integer> getVendorAdvancesByVendorCurrency(int vendorId, int currencyId) throws ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<Integer> vendorAdvances = new ArrayList<Integer>();
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(PaymentNonCoreConstants.GET_VENDOR_ADVANCES_FOR_VENDOR_CURRENCY);
			preparedStatement.setInt(1, vendorId);
			preparedStatement.setInt(2, currencyId);
			preparedStatement.setString(3, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_PARTIALLY_ADJUSTED);
			preparedStatement.setString(5, CommonConstants.STATUS_AS_ADJUSTED);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				vendorAdvances.add(rs.getInt(1));
			}
		} catch (Exception e) {
			logger.error("Error during getPaymentTypes", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return vendorAdvances;
	}

	public List<CreditDetailsVo> getCreditDetailsDropDown(int organizationId, int vendorId, int currencyId,
			int paymentId) throws ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<CreditDetailsVo> listVos = new ArrayList<CreditDetailsVo>();
		StringBuilder sqlQuery = new StringBuilder(PaymentNonCoreConstants.GET_CREDIT_DETAILS_DROPDOWN);
//		String query = PaymentNonCoreConstants.GET_CREDIT_DETAILS_DROPDOWN;
		try {
			con = getAccountsPayable();
			String invoiceIds = "";
			if (paymentId > 0) {
				PaymentNonCoreVo paymentVo = new PaymentNonCoreVo();
				paymentVo.setId(paymentId);
				paymentVo = fetchBillDetails(paymentVo, con);

				for (PaymentNonCoreBaseVo payment : paymentVo.getPayments()) {
					if (payment.getType() != null && payment.getType().equalsIgnoreCase("advance")) {
						CreditDetailsVo credit = getCreditDetail(Integer.parseInt(payment.getBillRef()));
						invoiceIds += credit.getId() + ",";
						listVos.add(credit);
					}

				}
				if (listVos != null && listVos.size() > 0 && invoiceIds.length() > 1) {
					sqlQuery.append(" and p1.id NOT IN (");
					sqlQuery.append(invoiceIds);
					sqlQuery.setLength(sqlQuery.length() - 1);// Removing last ,
					sqlQuery.append(")");
				}
			}
			logger.info(sqlQuery);
			preparedStatement = con.prepareStatement(sqlQuery.toString());
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setInt(2, vendorId);
			preparedStatement.setString(3, CommonConstants.STATUS_AS_ACTIVE);
			preparedStatement.setString(4, CommonConstants.STATUS_AS_PARTIALLY_ADJUSTED);
			preparedStatement.setInt(5, currencyId);
			preparedStatement.setDouble(6, 0.0);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				CreditDetailsVo creditDetail = new CreditDetailsVo();
				creditDetail.setId(rs.getInt(1));
				creditDetail.setReference(rs.getString(2));
				creditDetail.setOriginalAmt(rs.getString(3));
				creditDetail.setAvailableAmt(rs.getString(4));
				creditDetail.setVendorId(rs.getInt(5));
				creditDetail.setName(rs.getString(2));
				creditDetail.setValue(rs.getInt(1));
				listVos.add(creditDetail);
			}
		} catch (Exception e) {
			logger.error("Error during getCreditDetailsDropDown", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return listVos;
	}

	public List<PaymentModeDropDownVo> addValueFieldToPaymentMode(List<PaymentModeVo> paymentModes) {
		List<PaymentModeDropDownVo> paymentModeDropdown = new ArrayList<PaymentModeDropDownVo>();
		for (PaymentModeVo payment : paymentModes) {
			PaymentModeDropDownVo pmDropdown = new PaymentModeDropDownVo();
			pmDropdown.setId(payment.getId());
			pmDropdown.setName(payment.getName());
			pmDropdown.setChild(payment.getAccountsList());
			pmDropdown.setValue(payment.getId());
			paymentModeDropdown.add(pmDropdown);
		}
		return paymentModeDropdown;
	}

	public List<PaymentNonCoreGstDetailsDropDownVo> addValueToGstDetails(List<TaxRateMappingVo> gstDetails) {
		List<PaymentNonCoreGstDetailsDropDownVo> gstDetailsDropdownList = new ArrayList<PaymentNonCoreGstDetailsDropDownVo>();
		for (TaxRateMappingVo payment : gstDetails) {
			PaymentNonCoreGstDetailsDropDownVo p = new PaymentNonCoreGstDetailsDropDownVo();
			p.setBase(payment.isBase());
			p.setCreateTs(payment.getCreateTs());
			p.setId(payment.getId());
			p.setInter(payment.isInter());
			p.setName(payment.getName());
			p.setOrganizationId(payment.getOrganizationId());
			p.setRate(payment.getRate());
			p.setRoleName(payment.getRoleName());
			p.setStatus(payment.getStatus());
			p.setSuperAdmin(payment.isSuperAdmin());
			p.setTaxRateTypeId(payment.getTaxRateTypeId());
			p.setTaxRateTypeName(payment.getTaxRateTypeName());
			p.setUpdateTs(payment.getUpdateTs());
			p.setValue(payment.getId());
			gstDetailsDropdownList.add(p);
		}
		return gstDetailsDropdownList;
	}

	public List<PaymentNonCoreBillDetailsDropDownVo> convertBillDetailsToDropdownValue(
			List<InvoiceListVo> billDetails) {
		List<PaymentNonCoreBillDetailsDropDownVo> billDetailsList = new ArrayList<PaymentNonCoreBillDetailsDropDownVo>();
		for (InvoiceListVo bill : billDetails) {
			if (bill.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_OPEN)
					|| bill.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_PARTIALLY_PAID)
					|| bill.getStatus().equalsIgnoreCase(CommonConstants.STATUS_AS_ACTIVE)
					|| bill.getStatus().equalsIgnoreCase(CommonConstants.DISPLAY_STATUS_AS_ACTIVE)) {
				PaymentNonCoreBillDetailsDropDownVo bd = new PaymentNonCoreBillDetailsDropDownVo();
				bd.setId(bill.getInvoiceId());
				bd.setVendorDisplayName(bill.getVendorDisplayName());
				bd.setAmount(bill.getAmount());
				bd.setBalanceDue(bill.getBalanceDue());
				bd.setDueDate(bill.getDueDate());
				bd.setInvoiceDate(bill.getInvoiceDate());
				bd.setInvoiceNo(bill.getInvoiceNo());
				bd.setIsVendorEditable(bill.getIsVendorEditable());
				bd.setName(bill.getInvoiceNo());
				bd.setOrgName(bill.getOrgName());
				bd.setPoReferenceNo(bill.getPoReferenceNo());
				bd.setQuick(bill.getIsQuick());
				bd.setRoleName(bill.getRoleName());
				bd.setStatus(bill.getStatus());
				bd.setUserId(bill.getUserId());
				bd.setValue(bill.getInvoiceId());
				billDetailsList.add(bd);
			}
		}
		return billDetailsList;
	}

	public Integer getPaymentTypeId(String paymentType) throws ApplicationException {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer id = null;
		try {
			con = getFinanceCommon();
			ps = con.prepareStatement(PaymentNonCoreConstants.GET_PAYMENT_TYPE_ID_BY_STRING);
			ps.setString(1, paymentType);
			rs = ps.executeQuery();
			while (rs.next()) {
				id = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.error("Error during getPaymentTypeId", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, ps, con);
		}
		return id;
	}

//	public List<ListPaymentNonCoreVo> getAllPaymentsOfAnOrganizationForUserAndRoleByType(int organizationId,
//			String userId, String roleName, String type) throws ApplicationException {
//
//		logger.info("Entry into method: getAllPaymentsOfAnOrganizationForUserAndRoleByType");
//		Connection con = null;
//		PreparedStatement preparedStatement = null;
//		ResultSet rs = null;
//		List<ListPaymentNonCoreVo> totalList = new ArrayList<ListPaymentNonCoreVo>();
//		try {
//			con = getAccountsPayable();
//			String query = "";
//			Integer id = getPaymentTypeId(type);
//			if (id != null) {
//				if (roleName.equals("Super Admin")) {
//					query = PaymentNonCoreConstants.GET_PAYMENTS_OF_ORG_BY_TYPE;
//				} else {
//					query = PaymentNonCoreConstants.GET_PAYMENTS_OF_ORG_USER_ROLE_BY_TYPE;
//				}
//				logger.info(query);
//				preparedStatement = con.prepareStatement(query);
//				preparedStatement.setInt(1, organizationId);
//				preparedStatement.setInt(2, id);
//
//				if (!(roleName.equals("Super Admin"))) {
//					preparedStatement.setString(3, userId);
//					preparedStatement.setString(4, roleName);
//				}
//				rs = preparedStatement.executeQuery();
//				while (rs.next()) {
//					ListPaymentNonCoreVo listVo = new ListPaymentNonCoreVo();
//					listVo.setId(rs.getInt(1));
//					Connection con1 = getUserMgmConnection();
//					String dateFormat = organizationDao.getDefaultDateForOrganization(organizationId, con1);
//					closeResources(null, null, con1);
//					if (dateFormat != null && dateFormat.length() > 0) {
//						listVo.setPaymentDate(rs.getDate(2) != null
//								? DateConverter.getInstance().convertDateToGivenFormat(rs.getDate(2), dateFormat)
//								: null);
//					}
//
//					listVo.setPaymentRefNo(rs.getString(3));
//					listVo.setPaymentType(rs.getString(4));
//
//					String paymentMode = financeCommonDao.getPaymentModeById(rs.getInt(10));
//					if (null != paymentMode && paymentMode.equalsIgnoreCase("Cash")) {
//						BankMasterCashAccountVo cashMasterAccountVo = bankMasterDao
//								.getBankMasterCashAccountById(rs.getInt(5));
//						listVo.setPaidVia(cashMasterAccountVo.getCashAccountName());
//					} else if (null != paymentMode && (paymentMode.equalsIgnoreCase("Bank Transfer")
//							|| paymentMode.equalsIgnoreCase("Cheque&Demand Draft"))) {
//						BankMasterAccountVo bankMasterAccountVo = bankMasterDao.getBankMasterAccountsById(rs.getInt(5));
//						listVo.setPaidVia(bankMasterAccountVo.getBankName());
//					} else if (null != paymentMode && paymentMode.equalsIgnoreCase("Wallet")) {
//						listVo.setPaidVia(bankMasterDao.getBankMasterWalletsById(rs.getInt(5)).getWalletAccountName());
//					} else if (null != paymentMode && paymentMode.equalsIgnoreCase("Credit Card")) {
//						listVo.setPaidVia(bankMasterDao.getBankMasterCardsById(rs.getInt(5)).getAccountName());
//					}
//
//					String contactType = rs.getString(9);
//
//					PaymentTypeVo contact = new PaymentTypeVo();
//					if (contactType != null) {
//						switch (type) {
//						case PaymentNonCoreConstants.TDS_PAYMENTS:
//						case PaymentNonCoreConstants.GST_PAYMENTS:
//							StatutoryBodyVo stat = statutoryBodyDao.getStatutoryBodyById(rs.getInt(14));
//							if (stat != null && stat.getId() != null && stat.getName() != null) {
//								contact.setId(stat.getId());
//								contact.setValue(stat.getId());
//								contact.setName(stat.getName());
//							}
//							break;
//						case PaymentNonCoreConstants.CUSTOMER_REFUNDS:
//							CustomerVo cust = customerDao.getCustomerById(rs.getInt(15));
//							if (cust != null && cust.getId() != null
//									&& cust.getPrimaryInfo().getCustomerDisplayName() != null) {
//								contact.setId(cust.getId());
//								contact.setValue(cust.getId());
//								contact.setName(cust.getPrimaryInfo().getCustomerDisplayName());
//							}
//							break;
//						case PaymentNonCoreConstants.BILL_PAYMENTS:
//						case PaymentNonCoreConstants.VENDOR_ADVANCE:
//							VendorVo vendor = vendorDao.getVendorByVendorId(rs.getInt(13));
//							if (vendor != null && vendor.getVendorGeneralInformation().getVendorDisplayName() != null) {
//								contact.setName(vendor.getVendorGeneralInformation().getVendorDisplayName());
//							}
//							break;
//						}
//					}
//
//					listVo.setContact(contact.getName());
//
//					String amountPaid = rs.getString(11);
//
//					CurrencyVo currencyVo = currencyDao.getCurrency(rs.getInt(12));
//					if (amountPaid != null && amountPaid.length() > 0 && currencyVo != null) {// Set symbol
//						listVo.setAmount(currencyVo.getSymbol() != null ? currencyVo.getSymbol() + " " + amountPaid
//								: amountPaid);
//					} else {
//						listVo.setAmount(amountPaid);
//					}
//
//					listVo.setStatus(rs.getString(8));
//					listVo.setContactType(rs.getString(9));
//					listVo.setPaymentModeId(rs.getInt(10));
//
//					totalList.add(listVo);
//
//				}
//			}
//
//		} catch (Exception e) {
//			logger.error("Error during getAllPaymentsOfAnOrganizationForUserAndRole", e);
//			throw new ApplicationException(e);
//		} finally {
//			closeResources(rs, preparedStatement, con);
//		}
//
//		return totalList;
//
//	}

	public String getVendorAdvanceReferenceNo(Integer id, Integer orgId) throws ApplicationException {
		String refNo = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getAccountsPayable();
			preparedStatement = con.prepareStatement(PaymentNonCoreConstants.GET_VENDOR_ADVANCE_REFERENCE_BY_ID);
			preparedStatement.setInt(1, orgId);
			preparedStatement.setInt(2, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				refNo = rs.getString(1);
			}
		} catch (Exception e) {
			logger.info("Error in getInvoiceOrderNo  ", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

		return refNo;
	}

	public CreditDetailsVo getCreditDetail(int paymentId) throws ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		CreditDetailsVo creditDetail = new CreditDetailsVo();
		String query = PaymentNonCoreConstants.GET_CREDIT_DETAILS;
		try {
			con = getAccountsPayable();
			logger.info(query);
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, paymentId);

			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				creditDetail.setId(rs.getInt(1));
				creditDetail.setReference(rs.getString(2));
				creditDetail.setOriginalAmt(rs.getString(3));
				creditDetail.setAvailableAmt(rs.getString(4));
				creditDetail.setVendorId(rs.getInt(5));
				creditDetail.setName(rs.getString(2));
				creditDetail.setValue(rs.getInt(1));
			}
		} catch (Exception e) {
			logger.error("Error during getCreditDetailsDropDown", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return creditDetail;
	}

	public List<ListPaymentNonCoreVo> getTop5PaymentsOfAnOrganizationForUserRoleType(int organizationId, String userId,
			String roleName, int type) throws ApplicationException {

		logger.info("Entry into method: getTop5PaymentsOfAnOrganizationForUserRoleType");
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		List<ListPaymentNonCoreVo> totalList = new ArrayList<ListPaymentNonCoreVo>();
		try {
			con = getAccountsPayable();
			String query = "";

			if (type > 0 && roleName.equals("Super Admin")) {
				query = PaymentNonCoreConstants.GET_TOP5_PAYMENTS_OF_ORG;
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setInt(1, organizationId);
				preparedStatement.setInt(2, type);
			} else if (type > 0) {
				query = PaymentNonCoreConstants.GET_TOP5_PAYMENTS_OF_ORG_USER_ROLE;
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setInt(1, organizationId);
				preparedStatement.setInt(2, type);

				preparedStatement.setString(3, userId);
				preparedStatement.setString(4, roleName);

			} else if (roleName.equals("Super Admin")) {
				query = PaymentNonCoreConstants.GET_TOP5_PAYMENTS_OF_ORG_SUMMARY;
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setInt(1, organizationId);
			} else {
				query = PaymentNonCoreConstants.GET_TOP5_PAYMENTS_OF_ORG__SUMMARY_USER_ROLE;
				preparedStatement = con.prepareStatement(query);
				preparedStatement.setInt(1, organizationId);
				preparedStatement.setString(2, userId);
				preparedStatement.setString(3, roleName);
			}
			logger.info(query);

			rs = preparedStatement.executeQuery();
			totalList = generatePaymentList(rs);

		} catch (Exception e) {
			logger.error("Error during getAllPaymentsOfAnOrganizationForUserAndRole", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}

		return totalList;

	}

	public PaymentAdviceVo getPaymentAdvice(int organizationId, String userId, String roleName, int id) throws ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		PaymentAdviceVo advice = new PaymentAdviceVo();
		String query = null;
		if (!roleName.equalsIgnoreCase(CommonConstants.ROLE_SUPER_ADMIN)) {
			query = PaymentNonCoreConstants.GET_PAYMENT_ADVICE_USER_ROLE;
		} else {
			query = PaymentNonCoreConstants.GET_PAYMENT_ADVICE;
		}
		try {
			con = getAccountsPayable();
			logger.info(query);
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setInt(2, id);
			if (!roleName.equalsIgnoreCase(CommonConstants.ROLE_SUPER_ADMIN)) {
				preparedStatement.setString(3, userId);
				preparedStatement.setString(4, roleName);
			}
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				advice.setOrgName(rs.getString("org_name"));
				
				ContactAddressVo orgAddress = new ContactAddressVo();
				orgAddress.setContact(rs.getString("org_name"));
				orgAddress.setAddr1(rs.getString("org_address_1"));
				orgAddress.setAddr2(rs.getString("org_address_2"));
				orgAddress.setCity(rs.getString("org_city"));
				orgAddress.setState(rs.getString("org_state"));
				orgAddress.setCountry(rs.getString("org_country"));
				orgAddress.setPinCode(rs.getString("org_pincode"));
				if (rs.getString("org_gst") != null)
					orgAddress.setGstNo(rs.getString("org_gst"));
				else
					orgAddress.setGstNo("");
				advice.setOrgAddress(orgAddress);
				
				advice.setPaymentNumber(rs.getString("payment_number"));
				
				String dateFormat = rs.getString("date_format");
				if (dateFormat != null && dateFormat.length() > 0) {
					advice.setPaymentDate(rs.getDate("payment_date") != null
							? DateConverter.getInstance().convertDateToGivenFormat(rs.getDate("payment_date"), dateFormat)
							: null);
				}
				advice.setPlaceOfSupplyName(rs.getString("state_name"));
				advice.setPlaceOfSupplyCode(rs.getString("state_code"));
				
				advice.setBankReferenceNumber(rs.getString("bank_reference_number"));
				String paymentType = rs.getString("payment_type");
				String contactType = rs.getString("contact_type");
				List<ContactAddressVo> contacts = new ArrayList<ContactAddressVo>();
				ContactAddressVo contact = new ContactAddressVo();
				switch (paymentType) {
				case PaymentNonCoreConstants.TDS_PAYMENTS:
				case PaymentNonCoreConstants.GST_PAYMENTS:					
					contact.setContact(rs.getString("statutory"));
					contact.setAddr1(rs.getString("statutory_address_1"));
					contact.setAddr2(rs.getString("statutory_address_2"));
					contact.setCity(rs.getString("statutory_city"));
					contact.setState(rs.getString("state"));
					contact.setCountry(rs.getString("country"));
					contact.setPinCode(rs.getString("statutory_pincode"));
					contact.setGstNo("");
					contacts.add(contact);
					break;
				case PaymentNonCoreConstants.CUSTOMER_REFUNDS:
					contact.setContact(rs.getString("customer"));
					contact.setAddr1(rs.getString("customer_address_1"));
					contact.setAddr2(rs.getString("customer_address_2"));
					contact.setCity(rs.getString("customer_city"));
					contact.setState(rs.getString("state"));
					contact.setCountry(rs.getString("country"));
					contact.setPinCode(rs.getString("customer_pincode"));
					if (rs.getString("customer_gst") != null)
						contact.setGstNo(rs.getString("customer_gst"));
					else
						contact.setGstNo("");
					contacts.add(contact);
					break;
				case PaymentNonCoreConstants.BILL_PAYMENTS:
				case PaymentNonCoreConstants.VENDOR_ADVANCE:
					contact.setContact(rs.getString("vendor"));
					contact.setAddr1(rs.getString("vendor_address_1"));
					contact.setAddr2(rs.getString("vendor_address_2"));
					contact.setCity(rs.getString("vendor_city"));
					contact.setState(rs.getString("state"));
					contact.setCountry(rs.getString("country"));
					contact.setPinCode(rs.getString("vendor_pincode"));
					if (rs.getString("vendor_gst") != null)
						contact.setGstNo(rs.getString("vendor_gst"));
					else
						contact.setGstNo("");
					advice.setPlaceOfSupplyName(rs.getString("ap_state_name"));
					advice.setPlaceOfSupplyCode(rs.getString("ap_state_code"));
					contacts.add(contact);
					break;
				case PaymentNonCoreConstants.EMPLOYEE_PAYMENTS:
					String empString = rs.getString("emp_name");
					if (empString != null && empString.length() > 0) {
						String[] empArray = empString.split(",");
						for (String emp : empArray) {
							ContactAddressVo contactEmp = new ContactAddressVo();
							contactEmp.setContact(emp);
							contactEmp.setGstNo("");
							contacts.add(contactEmp);
						}
					}
					break;
				case PaymentNonCoreConstants.OTHERS:
					if (contactType != null) {
						switch (contactType) {
						case PaymentNonCoreConstants.STATUTORY_BODY:
							contact.setContact(rs.getString("statutory"));
							contact.setAddr1(rs.getString("statutory_address_1"));
							contact.setAddr2(rs.getString("statutory_address_2"));
							contact.setCity(rs.getString("statutory_city"));
							contact.setState(rs.getString("state"));
							contact.setCountry(rs.getString("country"));
							contact.setPinCode(rs.getString("statutory_pincode"));
							contact.setGstNo("");
							contacts.add(contact);
							break;
						case PaymentNonCoreConstants.CUSTOMER:
							contact.setContact(rs.getString("customer"));
							contact.setAddr1(rs.getString("customer_address_1"));
							contact.setAddr2(rs.getString("customer_address_2"));
							contact.setCity(rs.getString("customer_city"));
							contact.setState(rs.getString("state"));
							contact.setCountry(rs.getString("country"));
							contact.setPinCode(rs.getString("customer_pincode"));
							if (rs.getString("customer_gst") != null)
								contact.setGstNo(rs.getString("customer_gst"));
							else
								contact.setGstNo("");
							contacts.add(contact);
							break;
						case PaymentNonCoreConstants.EMPLOYEE:
							contact.setContact(rs.getString("emp_name_other"));
							contact.setGstNo("");
							contacts.add(contact);
							break;
						case PaymentNonCoreConstants.VENDOR:
							contact.setContact(rs.getString("vendor"));
							contact.setAddr1(rs.getString("vendor_address_1"));
							contact.setAddr2(rs.getString("vendor_address_2"));
							contact.setCity(rs.getString("vendor_city"));
							contact.setState(rs.getString("state"));
							contact.setCountry(rs.getString("country"));
							contact.setPinCode(rs.getString("vendor_pincode"));
							if (rs.getString("vendor_gst") != null)
								contact.setGstNo(rs.getString("vendor_gst"));
							else
								contact.setGstNo("");
							contacts.add(contact);
							break;
						}
					}
				}
				advice.setPaidTo(contacts);
				advice.setPaymentMode(rs.getString("mode"));

				String paymentMode = rs.getString("mode");
				if (null != paymentMode && paymentMode.equalsIgnoreCase("Cash")) {
					advice.setPaidThrough(rs.getString("cash_name"));
				} else if (null != paymentMode && (paymentMode.equalsIgnoreCase("Bank Transfer")
						|| paymentMode.equalsIgnoreCase("Cheque&Demand Draft"))) {
					advice.setPaidThrough(rs.getString("bank_name"));
				} else if (null != paymentMode && paymentMode.equalsIgnoreCase("Wallet")) {
					advice.setPaidThrough(rs.getString("wallet_name"));
				} else if (null != paymentMode && paymentMode.equalsIgnoreCase("Credit Card")) {
					advice.setPaidThrough(rs.getString("account_name"));
				}
				
				String amountPaid = rs.getString("amount_paid");

				String currencySymbol = rs.getString("symbol");
				if (amountPaid != null && amountPaid.length() > 0 && currencySymbol != null) {// Set symbol
					advice.setAmountPaid(currencySymbol + " " + amountPaid);
				} else {
					advice.setAmountPaid(amountPaid);
				}
				
				if (amountPaid != null && amountPaid.contains(".")) {
					amountPaid =  amountPaid.split("\\.")[0];
				}
				
				String amountPaidInWords = EnglishNumberToWords.convert(Long.parseLong(amountPaid));
				if (amountPaidInWords != null && amountPaidInWords.length() > 0) {
					Character c = amountPaidInWords.charAt(0);
					c = Character.toUpperCase(c);
					amountPaidInWords = c + amountPaidInWords.substring(1) + " Only";
				}
				advice.setAmountPaidInWords(amountPaidInWords);
			}
		} catch (Exception e) {
			logger.error("Error during getPaymentAdvice", e);
			throw new ApplicationException(e);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return advice;
	
	}

}
