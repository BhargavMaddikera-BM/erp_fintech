package com.blackstrawai.externalintegration.banking.yesbank;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.BillsInvoiceDao;
import com.blackstrawai.ar.ArInvoiceDao;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.externalintegration.yesbank.WorkflowBankingVo;
import com.blackstrawai.externalintegration.yesbank.YesBankCustomerInformationVo;
import com.blackstrawai.externalintegration.yesbank.YesBankNewAccountSetupVo;
import com.blackstrawai.externalintegration.yesbank.Request.bulkpayments.BulkPaymentDataVo;
import com.blackstrawai.externalintegration.yesbank.Request.bulkpayments.BulkPaymentDomesticPaymentVo;
import com.blackstrawai.externalintegration.yesbank.Request.bulkpayments.BulkPaymentVo;
import com.blackstrawai.externalintegration.yesbank.Request.bulkpayments.BulkPaymentsListVo;
import com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer.PaymentTransferCreditorAccountVo;
import com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer.PaymentTransferDebtorAccountVo;
import com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer.PaymentTransferDomesticPaymentsVo;
import com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer.PaymentTransferInitiationVo;
import com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer.PaymentTransferInstructedAmountVo;
import com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer.PaymentTransferRemittanceInformationVo;
import com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer.PaymentTransferVo;
import com.blackstrawai.externalintegration.yesbank.Response.YesBankAuthorizationVo;
import com.blackstrawai.externalintegration.yesbank.Response.bulkpayments.BulkPaymentsResponse;
import com.blackstrawai.externalintegration.yesbank.Response.bulkpayments.BulkTransactionDetailVo;
import com.blackstrawai.externalintegration.yesbank.Response.bulkpayments.BulkTransactionVo;
import com.blackstrawai.externalintegration.yesbank.Response.payments.PaymentTransactionVo;
import com.blackstrawai.externalintegration.yesbank.Response.paymenttransfer.PaymentTransferResponseVo;
import com.blackstrawai.externalintegration.yesbank.beneficiary.BeneficiaryTransactionResponseVo;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class YesBankIntegrationDao extends BaseDao {

	private final Logger logger = Logger.getLogger(YesBankIntegrationDao.class);

	private final BillsInvoiceDao billsInvoiceDao;
	private final ArInvoiceDao arInvoiceDao;

	public YesBankIntegrationDao(BillsInvoiceDao billsInvoiceDao, ArInvoiceDao arInvoiceDao) {
		this.billsInvoiceDao = billsInvoiceDao;
		this.arInvoiceDao = arInvoiceDao;
	}

	public int requestAPIBanking(YesBankCustomerInformationVo informationVo)
			throws ApplicationException {

		Connection con = null;
		PreparedStatement preparedStatement = null;

		try {
			con = getBankingConnection();
			String apiBankingrequestdetail = getAPIBankingRequesteId(informationVo);
			if (Objects.isNull(apiBankingrequestdetail)) {
				// To request API Banking
				preparedStatement = con.prepareStatement(YesBankConstants.REQUEST_API_BANKING);
				preparedStatement.setString(1, informationVo.getCustomerId());
				preparedStatement.setString(2, informationVo.getAccountNo());
				preparedStatement.setString(3, informationVo.getAccountName());
				preparedStatement.setString(4, informationVo.getBranch());
				preparedStatement.setString(5, informationVo.getMobileNo());
				preparedStatement.setString(6, informationVo.getEcollectCode());
				preparedStatement.setString(7, "N");
				preparedStatement.setString(8, CommonConstants.STATUS_AS_ACTIVE);
				preparedStatement.setInt(9, informationVo.getOrgId());
				preparedStatement.setString(10, informationVo.getRoleName());
				preparedStatement.setDate(11, new Date(System.currentTimeMillis()));
				preparedStatement.setString(12, informationVo.getUserId());
				int rowAffected = preparedStatement.executeUpdate();
				logger.info("Customer information return value>>>" + rowAffected);
				return rowAffected;
			} else {
				int requestId = 0;
				String isEnabled = null;
				if (Objects.nonNull(apiBankingrequestdetail)) {
					String[] details = apiBankingrequestdetail.split("~");
					requestId =
							Objects.nonNull(details)
							&& details.length > 0
							&& Objects.nonNull(details[0])
							&& !details[0].isEmpty()
							? Integer.valueOf(details[0])
									: 0;
							isEnabled =
									Objects.nonNull(details)
									&& details.length > 0
									&& Objects.nonNull(details[0])
									&& !details[0].isEmpty()
									? details[1]
											: null;
				}
				logger.info("requestId>>>" + requestId + "isEnabled>>>" + isEnabled);
				if (requestId != 0 && "N".equals(isEnabled)) {
					throw new ApplicationException("Request to enable API banking is already available for this account");
				}
				if (requestId != 0 && "Y".equals(isEnabled)) {
					throw new ApplicationException("API banking is already available for this account");
				}
			} 


		} catch (Exception ex) {
			logger.error("Exception in saving the customer information>>>>" + ex);
			throw new ApplicationException(ex.getMessage());
		} finally {
			closeResources(null, preparedStatement, con);
		}
		return 0;
	}

	public YesBankCustomerInformationVo enableAPIBanking(YesBankCustomerInformationVo informationVo)
			throws ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		if (informationVo != null) {
			try {
				con = getBankingConnection();
				String apiBankingrequestdetail = getAPIBankingRequesteId(informationVo);
				if (Objects.isNull(apiBankingrequestdetail)) {
					// When already API Banking available for customer and enabling through system
					preparedStatement = con.prepareStatement(YesBankConstants.ENABLE_API_BANKING);
					preparedStatement.setString(1, informationVo.getCustomerId());
					preparedStatement.setString(2, informationVo.getAccountNo());
					preparedStatement.setString(3, informationVo.getUserName());
					preparedStatement.setString(4, informationVo.getAuthKey1());
					preparedStatement.setString(5, informationVo.getAuthKey2());
					preparedStatement.setString(6, informationVo.getEcollectCode());
					preparedStatement.setString(7, "Y");
					preparedStatement.setString(8, CommonConstants.STATUS_AS_ACTIVE);
					preparedStatement.setInt(9, informationVo.getOrgId());
					preparedStatement.setString(10, informationVo.getRoleName());
					preparedStatement.setDate(11, new Date(System.currentTimeMillis()));
					preparedStatement.setString(12, informationVo.getUserId());
					preparedStatement.setString(13, informationVo.getMobileNo());       
					preparedStatement.executeUpdate();
				} else {
					int requestId = 0;
					String isEnabled = null;
					if (Objects.nonNull(apiBankingrequestdetail)) {
						String[] details = apiBankingrequestdetail.split("~");
						requestId =
								Objects.nonNull(details)
								&& details.length > 0
								&& Objects.nonNull(details[0])
								&& !details[0].isEmpty()
								? Integer.valueOf(details[0])
										: 0;
								isEnabled =
										Objects.nonNull(details)
										&& details.length > 0
										&& Objects.nonNull(details[0])
										&& !details[0].isEmpty()
										? details[1]
												: null;
					}
					logger.info("requestId>>>" + requestId + "isEnabled>>>" + isEnabled);

					// Activate when API banking not enabled but requested for API Banking
					if (requestId != 0 && Objects.equals("N", isEnabled)) {
						preparedStatement =
								con.prepareStatement(YesBankConstants.ENABLE_API_BANKING_WHEN_ALREADY_REQUESTED);
						preparedStatement.setString(1, informationVo.getUserName());
						preparedStatement.setString(2, informationVo.getAuthKey1());
						preparedStatement.setString(3, informationVo.getAuthKey2());
						preparedStatement.setString(4, "Y");
						preparedStatement.setString(5, informationVo.getEcollectCode());
						preparedStatement.setString(6, informationVo.getMobileNo());
						preparedStatement.setInt(7, requestId);
						preparedStatement.executeUpdate();
					}

					if (requestId != 0 && Objects.equals("Y", isEnabled)) {
						throw new ApplicationException(
								"API Banking is already enabled for the requested account");
					}
				}

			} catch (Exception ex) {
				logger.error("Exception in enableAPIBanking>>>>" + ex);
				throw new ApplicationException(ex.getMessage());
			} finally {
				closeResources(rs, preparedStatement, con);
			}
		}
		return informationVo;
	}

	private String getAPIBankingRequesteId(YesBankCustomerInformationVo customerInfo)
			throws ApplicationException {
		String apiBankingRequesteDetail = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			con = getBankingConnection();

			preparedStatement = con.prepareStatement(YesBankConstants.GET_API_BANKING_REQUESTED_ID);
			preparedStatement.setInt(1, customerInfo.getOrgId());
			preparedStatement.setString(2, customerInfo.getCustomerId());
			preparedStatement.setString(3, customerInfo.getAccountNo());
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				apiBankingRequesteDetail = "" + rs.getInt(1) + "~" + rs.getString(2);
			}
			logger.info("Result value>>>" + apiBankingRequesteDetail);
		} catch (Exception ex) {
			logger.error("Exception in isAPIBankingRequestedn>>>>" + ex);
			throw new ApplicationException(ex);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return apiBankingRequesteDetail;
	}

	public YesBankAuthorizationVo getCustomerAuthorization(
			String orgId, String customerId, String accountNumber,String userId,String roleName) throws ApplicationException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		YesBankAuthorizationVo yesBankAuthorizationVo = null;
		try {

			con = getBankingConnection();
			preparedStatement = con.prepareStatement(YesBankConstants.RETRIEVE_USER_INFO);
			preparedStatement.setString(1, orgId);
			preparedStatement.setString(2, customerId);
			preparedStatement.setString(3, accountNumber);
			preparedStatement.setString(4, userId);
			preparedStatement.setString(5, roleName);

			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				yesBankAuthorizationVo = new YesBankAuthorizationVo();
				yesBankAuthorizationVo.setUserName(rs.getString(1));
				yesBankAuthorizationVo.setAuthKey(rs.getString(2) + rs.getString(3));
			}
			return yesBankAuthorizationVo;
		} catch (Exception ex) {
			logger.error("Exception in retrieving user information>>>>" + ex);
			throw new ApplicationException(ex);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
	}

	public int savePaymentTransferInfo(
			PaymentTransferVo paymentTransferVo,
			String requestJson,
			PaymentTransferResponseVo paymentTransferResponseVo,
			String responseJson,
			String organizationId,
			String userId,
			String roleName,
			String uiJsonRequest,boolean isDraft)
					throws ApplicationException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		int paymentId=0;
		try {

			PaymentTransferInitiationVo initiation = paymentTransferVo.getData().getInitiation();

			connection = getBankingConnection();
			preparedStatement =
					connection.prepareStatement(
							YesBankConstants.SAVE_PAYMENT_INFO, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, initiation.getInstructionIdentification());
			preparedStatement.setString(2, paymentTransferVo.getData().getConsentId());
			preparedStatement.setString(
					3, initiation.getDebtorAccount().getIdentification());
			preparedStatement.setString(4, initiation.getClearingSystemIdentification());
			preparedStatement.setBigDecimal(
					5, new BigDecimal(initiation.getInstructedAmount().getAmount()));
			preparedStatement.setString(6, requestJson);
			preparedStatement.setString(7, responseJson);
			preparedStatement.setString(
					8, paymentTransferResponseVo!=null?paymentTransferResponseVo.getData().getTransactionIdentification():null);
			preparedStatement.setString(9, organizationId);
			preparedStatement.setString(10, roleName);
			preparedStatement.setString(11, userId);
			preparedStatement.setString(
					12, initiation.getCreditorAccount().getBeneficiaryId());
			preparedStatement.setString(
					13,
					initiation.getCreditorAccount().getBeneficiaryType());
			preparedStatement.setString(
					14,
					initiation
					.getRemittanceInformation()
					.getBillReference());
			preparedStatement.setString(
					15,
					initiation
					.getRemittanceInformation()
					.getPaymentReference());
			preparedStatement.setString(16, uiJsonRequest);
			preparedStatement.setString(17, isDraft?CommonConstants.STATUS_AS_DRAFT:"PENDING");
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					paymentId = rs.getInt(1);
				}
			}
			logger.info("Customer information return value>>>" + rowAffected);
			return paymentId;

		} catch (Exception ex) {
			logger.error("Exception while saving payment information>>>" + ex);
			throw new ApplicationException(ex);
		} finally {
			closeResources(rs, preparedStatement, connection);
		}
	}

	public int updatePaymentTransferInfo(
			PaymentTransferVo paymentTransferVo,
			String requestJson,
			PaymentTransferResponseVo paymentTransferResponseVo,
			String responseJson,
			String organizationId,
			String userId,
			String roleName,
			String uiJsonRequest,int id)
					throws ApplicationException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		int paymentId=0;
		try {

			PaymentTransferInitiationVo initiation = paymentTransferVo.getData().getInitiation();

			connection = getBankingConnection();
			preparedStatement =connection.prepareStatement(YesBankConstants.UPDATE_PAYMENT_INFO, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, initiation.getInstructionIdentification());
			preparedStatement.setString(2, paymentTransferVo.getData().getConsentId());
			preparedStatement.setString(3, paymentTransferVo.getData().getInitiation().getDebtorAccount().getIdentification());
			preparedStatement.setString(4, initiation.getClearingSystemIdentification());
			preparedStatement.setBigDecimal(5, new BigDecimal(initiation.getInstructedAmount().getAmount()));
			preparedStatement.setString(6, requestJson);
			preparedStatement.setString(7, responseJson);
			preparedStatement.setString(8, paymentTransferResponseVo.getData().getTransactionIdentification());
			preparedStatement.setString(9, organizationId);
			preparedStatement.setString(10, roleName);
			preparedStatement.setString(11, userId);
			preparedStatement.setString(12, paymentTransferVo.getData().getInitiation().getCreditorAccount().getBeneficiaryId());
			preparedStatement.setString(13, paymentTransferVo.getData().getInitiation().getCreditorAccount().getBeneficiaryType());
			preparedStatement.setString(14,paymentTransferVo.getData().getInitiation().getRemittanceInformation().getBillReference());
			preparedStatement.setString(15,paymentTransferVo.getData().getInitiation().getRemittanceInformation().getPaymentReference());
			preparedStatement.setString(16, uiJsonRequest);
			preparedStatement.setString(17,"PENDING");
			preparedStatement.setInt(18, id);
			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					paymentId = rs.getInt(1);
				}
			}
			logger.info("Customer information return value>>>" + rowAffected);
			return paymentId;

		} catch (Exception ex) {
			logger.error("Exception while saving payment information>>>" + ex);
			throw new ApplicationException(ex);
		} finally {
			closeResources(rs, preparedStatement, connection);
		}
	}

	public int updateBulkPaymentTransferInfo(
			String uniqueIdentifier,
			String responseJson,
			String organizationId,
			String userId,
			String roleName,
			String status,
			String fileIdentifier,
			String customerId)
					throws ApplicationException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		int paymentId=0;
		try {
			logger.info("Update Sigle txn of bulk api ");

			connection = getBankingConnection();
			preparedStatement =connection.prepareStatement(YesBankConstants.UPDATE_SINGLE_TXN_BULK_PAYMENT, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, responseJson);
			preparedStatement.setString(2,status);
			preparedStatement.setString(3,fileIdentifier);
			preparedStatement.setDate(4, new Date(System.currentTimeMillis()));
			preparedStatement.setString(5, userId);
			preparedStatement.setString(6, roleName);
			preparedStatement.setString(7, organizationId);
			preparedStatement.setString(8,uniqueIdentifier);
			preparedStatement.setString(9,customerId);

			int rowAffected = preparedStatement.executeUpdate();
			if (rowAffected == 1) {
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					paymentId = rs.getInt(1);
				}
			}
			logger.info("Customer information return value>>>" + rowAffected);
			return paymentId;

		} catch (Exception ex) {
			logger.error("Exception while saving payment information>>>" + ex);
			throw new ApplicationException(ex);
		} finally {
			closeResources(rs, preparedStatement, connection);
		}
	}


	public List<BeneficiaryTransactionResponseVo> getPaymentTransactionList(
			String orgId, String accountNumber, String BeneficiaryId, String search,String roleName,String userId)
					throws ApplicationException {

		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {

			String query = YesBankConstants.RETRIEVE_PAYMENT_LIST;
			String searchQuery;
			if (!search.isEmpty()) {
				query = YesBankConstants.FILTER_PAYMENT_LIST;
				searchQuery =
						" and (date_format(ypi.create_ts, '%d/%m/%Y') like '%"
								+ search
								+ "%' "
								+ " or ypi.payment_transfer_reference_no_decifer like '%"
								+ search
								+ "%' "
								+ " or ybi.beneficiary_type like '%"
								+ search
								+ "%' "
								+ " or ybi.beneficiary_name like '%"
								+ search
								+ "%' "
								+ " or ypi.amount like '%"
								+ search
								+ "%' "
								+ " or ypi.status like '%"
								+ search
								+ "%') order by ypi.create_ts";

				query = query + searchQuery;
			}

			if (!BeneficiaryId.isEmpty()) {
				query = YesBankConstants.VIEW_BENEFICIARY_TRANSACTION_LIST;
			}
			con = getBankingConnection();
			logger.info(query);
			preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, orgId);
			preparedStatement.setString(2, roleName);
			preparedStatement.setString(3,userId);
			preparedStatement.setString(4, accountNumber);
			if (!BeneficiaryId.isEmpty()) {
				preparedStatement.setString(4, BeneficiaryId);
			}
			rs = preparedStatement.executeQuery();

			List<BeneficiaryTransactionResponseVo> paymentResponseList = new ArrayList<>();
			BeneficiaryTransactionResponseVo beneficiaryTransactionResponseVo;
			while (rs.next()) {
				beneficiaryTransactionResponseVo = new BeneficiaryTransactionResponseVo();
				beneficiaryTransactionResponseVo.setId(rs.getString(1));
				beneficiaryTransactionResponseVo.setBeneficiaryType(rs.getString(2));
				beneficiaryTransactionResponseVo.setResponseJson(rs.getString(3));
				if(rs.getString(4)!=null && (rs.getString(4).equalsIgnoreCase("pending"))){
					beneficiaryTransactionResponseVo.setStatus("Payment Initiated");
				}else{
					if(rs.getString(4)!=null) {
						String status="";
						switch (rs.getString(4)) {
						case CommonConstants.STATUS_AS_REJECT:
							status=CommonConstants.DISPLAY_STATUS_AS_REJECT;
							break;

						case CommonConstants.STATUS_AS_DRAFT:
							status=CommonConstants.DISPLAY_STATUS_AS_DRAFT;
							break;
						}
						beneficiaryTransactionResponseVo.setStatus(status);
					}
				}
				beneficiaryTransactionResponseVo.setAmount(rs.getString(5));
				String reqJson=rs.getString(6);
				if (reqJson != null ) {
					JSONParser parser = new JSONParser();
					JSONObject jsonData = (JSONObject) parser.parse(reqJson);
					if(jsonData!=null) {
						if(jsonData.get("Data")!=null) {
							logger.info("Data:"+jsonData.get("Data").toString());
							JSONObject data = (JSONObject) parser.parse(jsonData.get("Data").toString());
							if(data.get("Initiation")!=null) {
								JSONObject initiation = (JSONObject) parser.parse(data.get("Initiation").toString());
								if(initiation.get("CreditorAccount")!=null) {
									JSONObject creditorAccount = (JSONObject) parser.parse(initiation.get("CreditorAccount").toString());
									beneficiaryTransactionResponseVo.setBeneficiaryName(creditorAccount.get("SchemeName")!=null?creditorAccount.get("SchemeName").toString():"");				
								}
							}
						}

					}
				}
				beneficiaryTransactionResponseVo.setTransactionNumber(rs.getString(7));
				beneficiaryTransactionResponseVo.setNoOfTxn(1);
				//beneficiaryTransactionResponseVo.setTransactionDate("24/12/2020");
				//DateConverter.getInstance()
				//	                    .convertTimestampToDate(
				//		                        "yyyy-MM-dd'T'HH:mm:ss", "dd/MM/yyyy", rs.getString(8)));

				paymentResponseList.add(beneficiaryTransactionResponseVo);
			}
			return paymentResponseList;
		} catch (Exception ex) {
			logger.error("Exception in retrieving payment transaction list>>>>" , ex);
			throw new ApplicationException(ex);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
	}

	public Map<String, Map<String, String>> getBillsInvoiceDonutChartCalculation(int orgId)
			throws ApplicationException {
		Map<String, Map<String, String>> donutValues = new HashMap<String, Map<String, String>>();
		Map<String, String> billsMap = billsInvoiceDao.getStatusPercent(orgId);
		Map<String, String> invoiceMap = arInvoiceDao.getStatusPercent(orgId);
		if (Objects.nonNull(billsMap) && Objects.nonNull(invoiceMap)) {
			donutValues.put("Bills", billsMap);
			donutValues.put("Invoice", invoiceMap);
		}
		return donutValues;
	}

	public int saveNewAccountSetupInfo(YesBankNewAccountSetupVo yesBankCustomerInfoVo)
			throws ApplicationException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {

			connection = getBankingConnection();
			preparedStatement =
					connection.prepareStatement(
							YesBankConstants.SAVE_NEW_ACCOUNT_INFO, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, yesBankCustomerInfoVo.getOrganizationName());
			preparedStatement.setString(2, yesBankCustomerInfoVo.getConstitution());
			preparedStatement.setString(3, yesBankCustomerInfoVo.getContactPerson());
			preparedStatement.setString(4, yesBankCustomerInfoVo.getContactNo());
			preparedStatement.setString(5, yesBankCustomerInfoVo.getBranch());
			preparedStatement.setString(6, yesBankCustomerInfoVo.getOrganizationId());
			preparedStatement.setString(7, yesBankCustomerInfoVo.getRoleName());
			preparedStatement.setDate(8, new Date(System.currentTimeMillis()));
			preparedStatement.setString(9, yesBankCustomerInfoVo.getUserId());

			int rowAffected = preparedStatement.executeUpdate();

			logger.info("Customer information return value>>>" + rowAffected);
			return rowAffected;

		} catch (Exception ex) {
			logger.error("Exception while saving payment information>>>" + ex);
			throw new ApplicationException(ex);
		} finally {
			closeResources(null, preparedStatement, connection);
		}
	}

	public PaymentTransactionVo paymentTransaction(String organizationId, String transactionId)
			throws ApplicationException {

		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		PaymentTransactionVo paymentTransactionVo;
		try {

			con = getBankingConnection();
			preparedStatement = con.prepareStatement(YesBankConstants.PAYMENT_TRANSACTIONS);
			preparedStatement.setString(1, organizationId);
			preparedStatement.setString(2, transactionId);
			rs = preparedStatement.executeQuery();

			paymentTransactionVo = new PaymentTransactionVo();
			while (rs.next()) {
				paymentTransactionVo.setTransactionId(rs.getString(1));
				paymentTransactionVo.setUiRequestJson(rs.getString(2));
				paymentTransactionVo.setTransactionDate(rs.getString(3));
				paymentTransactionVo.setBankReferenceNo(rs.getString(4));
				paymentTransactionVo.setStatus(rs.getString(5));
			}

		} catch (Exception ex) {
			throw new ApplicationException(ex);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return paymentTransactionVo;
	}

	public YesBankCustomerInformationVo getYesBankAccountDetails(Integer id  , Integer orgId ) throws ApplicationException {
		YesBankCustomerInformationVo yesBankInfo = null;
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {

			con = getBankingConnection();
			preparedStatement = con.prepareStatement(YesBankConstants.GET_YES_BANK_ACCOUNT_DETAILS);
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				yesBankInfo = new YesBankCustomerInformationVo();
				yesBankInfo.setId(id);
				yesBankInfo.setCustomerId(rs.getString(2));
				yesBankInfo.setAccountNo(rs.getString(3));
				yesBankInfo.setEcollectCode(rs.getString(4));
				yesBankInfo.setAccountName(rs.getString(5));
				yesBankInfo.setBranch(rs.getString(6));
				yesBankInfo.setMobileNo(rs.getString(7));
				if(Objects.nonNull(rs.getString(8)) && "Y".equals(rs.getString(8))) {
					yesBankInfo.setIsApiBankingEnabled(true);
				}else {
					yesBankInfo.setIsApiBankingEnabled(false);
				}
				yesBankInfo.setIsPaymentAllowed(rs.getBoolean(10));
				yesBankInfo.setErpBankAccountId(rs.getInt(11));
				yesBankInfo.setIfsc(rs.getString(13));
				yesBankInfo.setOrgId(orgId);
			}

		} catch (Exception ex) {
			throw new ApplicationException(ex);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return yesBankInfo;


	}
	public String getYesBankRegisteredMobileNo(int organizationId, int userId, String roleName,String customerId,String accountName) throws ApplicationException {

		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String mobileNo=null;
		try {

			con = getBankingConnection();
			preparedStatement = con.prepareStatement(YesBankConstants.GET_MOBILE_NO_FOR_CUST);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setInt(2, userId);
			preparedStatement.setString(3, roleName);
			preparedStatement.setString(4, accountName);
			preparedStatement.setString(5, customerId);
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				mobileNo=rs.getString(1);
			}

		} catch (Exception ex) {
			throw new ApplicationException(ex);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return mobileNo;
	}

	public WorkflowBankingVo getWorfklowDataForPayment(int paymentId) throws ApplicationException {

		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		WorkflowBankingVo workflowBankingVo=new WorkflowBankingVo();
		try {

			con = getBankingConnection();
			preparedStatement = con.prepareStatement(YesBankConstants.GET_WORKFLOW_DATA_FOR_PAYMENT);
			preparedStatement.setInt(1, paymentId);
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				workflowBankingVo.setId(rs.getInt(1));
				workflowBankingVo.setAmount(rs.getString(2));

				PaymentTransferVo paymentTransferVo=new PaymentTransferVo();
				PaymentTransferDomesticPaymentsVo data=new PaymentTransferDomesticPaymentsVo();
				data.setConsentId(rs.getString(4));
				workflowBankingVo.setFinalJsonRequest(rs.getString(5));
				workflowBankingVo.setUiJsonRequest(rs.getString(6));
				workflowBankingVo.setOrganizationId(rs.getInt(7));
				workflowBankingVo.setRoleName(rs.getString(8));
				workflowBankingVo.setUserId(rs.getString(9));
				PaymentTransferInitiationVo initiation=new PaymentTransferInitiationVo();
				initiation.setInstructionIdentification(rs.getString(10));
				PaymentTransferDebtorAccountVo paymentTransferDebtorAccountVo=new PaymentTransferDebtorAccountVo();
				
				paymentTransferDebtorAccountVo.setIdentification(rs.getString(11));
				initiation.setDebtorAccount(paymentTransferDebtorAccountVo);
				initiation.setClearingSystemIdentification(rs.getString(12));
				PaymentTransferInstructedAmountVo paymentTransferInstructedAmountVo=new PaymentTransferInstructedAmountVo();
				paymentTransferInstructedAmountVo.setAmount(rs.getString(2));
				initiation.setInstructedAmount(paymentTransferInstructedAmountVo);  
				PaymentTransferCreditorAccountVo paymentTransferCreditorAccountVo=new PaymentTransferCreditorAccountVo();
				paymentTransferCreditorAccountVo.setBeneficiaryId(rs.getString(13));
				paymentTransferCreditorAccountVo.setBeneficiaryType(rs.getString(14));
				initiation.setCreditorAccount(paymentTransferCreditorAccountVo);
				PaymentTransferRemittanceInformationVo paymentTransferRemittanceInformationVo=new PaymentTransferRemittanceInformationVo();
				paymentTransferRemittanceInformationVo.setBillReference(rs.getString(15));
				paymentTransferRemittanceInformationVo.setPaymentReference(rs.getString(16));
				initiation.setRemittanceInformation(paymentTransferRemittanceInformationVo);
				data.setInitiation(initiation);
				paymentTransferVo.setData(data);
				String paymentType=rs.getString(17);
				workflowBankingVo.setPaymentType(paymentType);
				String fileIdentifier=rs.getString(18);
				workflowBankingVo.setFileIdentifier(fileIdentifier);
				if(paymentType!=null && paymentType.equalsIgnoreCase("Bulk")) {
					workflowBankingVo.setAmount(getBulkPaymentAmount(fileIdentifier));
				}
				workflowBankingVo.setPaymentTransferPaymentsVo(paymentTransferVo);
			}

		} catch (Exception ex) {
			throw new ApplicationException(ex);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return workflowBankingVo;
	}
	public boolean rejectOrDeclinePayment(int paymentId,boolean isReject,String paymentType,String fileIdentifier) throws ApplicationException {

		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		boolean result=false;
		try {

			con = getBankingConnection();
			if (paymentType != null && paymentType.equalsIgnoreCase("Single")) {
				preparedStatement = con.prepareStatement(YesBankConstants.REJECT_OR_DECLINE_PAYMENT);
				preparedStatement.setString(1,
						isReject ? CommonConstants.STATUS_AS_REJECT : CommonConstants.STATUS_AS_DECLINE);
				preparedStatement.setInt(2, paymentId);
			} else if (paymentType != null && paymentType.equalsIgnoreCase("Bulk")) {

				preparedStatement = con.prepareStatement(YesBankConstants.REJECT_OR_DECLINE_BULK_PAYMENT);
				preparedStatement.setString(1,
						isReject ? CommonConstants.STATUS_AS_REJECT : CommonConstants.STATUS_AS_DECLINE);
				preparedStatement.setString(2, fileIdentifier);

			}
			
			preparedStatement.executeUpdate();
			result=true;

		} catch (Exception ex) {
			logger.error("Error in rejectOrDeclinePayment");
			throw new ApplicationException(ex);
		} finally {
			closeResources(rs, preparedStatement, con);
		}
		return result;
	}



	public int saveBulkPaymentTransferInfo(BulkPaymentDataVo bulkPaymentData, String requestJson, BulkPaymentsResponse bulkpaymentResopnse,
			String responseJson, int orgId, String userId, String roleName, String uiJsonRequest, boolean isDraft, YesBankCustomerInformationVo debtorDetails,BulkPaymentDomesticPaymentVo paymentDetail) throws ApplicationException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		int paymentId=0;
		
		try {
			connection = getBankingConnection();
			logger.info("iterate before::"+bulkPaymentData.getDomesticPayments().size());
			if(!bulkPaymentData.getDomesticPayments().isEmpty()) {
				preparedStatement =
						connection.prepareStatement(
								YesBankConstants.SAVE_BULK_PAYMENT_INFO, Statement.RETURN_GENERATED_KEYS);
				logger.info("iterate"+paymentDetail.getInitiation().getInstructionIdentification());
				preparedStatement.setString(1, paymentDetail.getInitiation().getInstructionIdentification());
				preparedStatement.setString(2, paymentDetail.getConsentId());
				preparedStatement.setString(
						3,paymentDetail.getInitiation().getDebtorAccount().getIdentification());
				preparedStatement.setString(4, paymentDetail.getInitiation().getClearingSystemIdentification());
				preparedStatement.setBigDecimal(
						5, new BigDecimal(paymentDetail.getInitiation().getInstructedAmount().getAmount()));
				preparedStatement.setString(6, requestJson);
				preparedStatement.setString(7, responseJson);
				preparedStatement.setString(
						8, bulkpaymentResopnse!=null&&bulkpaymentResopnse.getData()!=null?bulkpaymentResopnse.getData().getFileIdentifier():null);
				preparedStatement.setInt(9, orgId);
				preparedStatement.setString(10, roleName);
				preparedStatement.setString(11, userId);
				preparedStatement.setString(
						12, paymentDetail.getInitiation().getCreditorAccount().getName());
				preparedStatement.setString(
						13,
						paymentDetail.getInitiation().getCreditorAccount().getIdentification());
				preparedStatement.setString(
						14,
						null);
				preparedStatement.setString(
						15,
						null);
				preparedStatement.setString(16, uiJsonRequest);
				preparedStatement.setString(17, isDraft?CommonConstants.STATUS_AS_DRAFT:"PENDING");
				preparedStatement.setString(
						18,"Bulk");
				preparedStatement.setString(
						19,
						bulkPaymentData.getFileIdentifier());
				preparedStatement.setString(
						20,
						paymentDetail.getInitiation().getCreditorAccount().getSchemeName());
				int rowAffected = preparedStatement.executeUpdate();
			
				if (rowAffected == 1) {
					rs = preparedStatement.getGeneratedKeys();
					if (rs.next()) {
						paymentId = rs.getInt(1);
					}
				}
				logger.info("Customer information return value>>>" + paymentId);
				return paymentId;
			}


		} catch (Exception ex) {
			logger.error("Exception while saving payment information>>>" , ex);
			throw new ApplicationException(ex);
		} finally {
			closeResources(rs, preparedStatement, connection);
		}
		return 0;
	}

	public List<BulkPaymentsListVo> getBulkPaymentsList(Integer orgId, Integer userId, String roleName, String accountNo) throws ApplicationException{
		List<BulkPaymentsListVo> tempBulkPayments = new ArrayList<BulkPaymentsListVo>();
		List<BulkPaymentsListVo> bulkPayments = new ArrayList<BulkPaymentsListVo>();

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			connection = getBankingConnection();
			preparedStatement =
					connection.prepareStatement(YesBankConstants.RETRIEVE_BULK_PAYMENT_LIST);
			if("Super Admin".equals(roleName)) {
				preparedStatement.setInt(1, orgId);
				preparedStatement.setString(2, accountNo);
			}else {
				preparedStatement =connection.prepareStatement(YesBankConstants.RETRIEVE_BULK_PAYMENT_LIST_USER_ROLE);
				preparedStatement.setInt(1, orgId);
				preparedStatement.setString(2, accountNo);
				preparedStatement.setString(3, roleName);
				preparedStatement.setInt(4, userId);
			}

			logger.info("preparedStatement"+ preparedStatement.toString());
			rs = preparedStatement.executeQuery();
			while(rs.next()) {
				BulkPaymentsListVo bulkPayment = new BulkPaymentsListVo();
				bulkPayment.setTxnNo(rs.getString(1));
				bulkPayment.setTxnDate(rs.getDate(3));
				bulkPayment.setTotalAmount(rs.getDouble(2));
				bulkPayment.setId(rs.getInt(5));
				tempBulkPayments.add(bulkPayment);
			}
			tempBulkPayments.sort(Comparator.comparing(BulkPaymentsListVo::getId).reversed());
			logger.info("List of temp bulk payments ::"+tempBulkPayments);

			Map<String, List<BulkPaymentsListVo>> txnMap = new HashMap<String, List<BulkPaymentsListVo>>();
			if(tempBulkPayments.size()>0) {
				txnMap = tempBulkPayments.stream()
						.collect(Collectors.groupingBy(BulkPaymentsListVo::getTxnNo));
			}
				
				
				txnMap = tempBulkPayments.stream()
						.collect(Collectors.groupingBy(BulkPaymentsListVo::getTxnNo, LinkedHashMap::new ,Collectors.toList()));
			logger.info("Map of temp bulk payments ::"+txnMap);
			if(!txnMap.isEmpty()) {
				txnMap.forEach((key , value ) ->{
					BulkPaymentsListVo payment = new BulkPaymentsListVo();
					payment.setTxnNo(key);
					payment.setNoOfTransaction(value.size());
					payment.setTxnDate(value.get(0)!=null ? value.get(0).getTxnDate() : null);
					Double totalSum = value.stream().mapToDouble(BulkPaymentsListVo::getTotalAmount).sum();
					payment.setTotalAmount(totalSum);
					bulkPayments.add(payment);
				});
			}
			
			logger.info("List of bulk payments ::"+bulkPayments);
		//	bulkPayments.sort(Comparator.comparing(BulkPaymentsListVo::getTxnTime).reversed());


		}
		catch (Exception ex) {
			logger.error("Exception while saving payment information>>>" + ex);
			throw new ApplicationException(ex);
		} finally {
			closeResources(rs, preparedStatement, connection);
		}

		return bulkPayments;



	}

	public BulkTransactionVo getBulkTransactionDetails(Integer organizationId, String roleName, String userId,String transactionNo) throws ApplicationException {
		BulkTransactionVo bulkTransaction = new BulkTransactionVo();
		List<BulkTransactionDetailVo> bulkPaymentsDetails = new ArrayList<BulkTransactionDetailVo>();
		 ObjectMapper objectMapper = new ObjectMapper();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			connection = getBankingConnection();
			preparedStatement =
					connection.prepareStatement(
							YesBankConstants.RETRIEVE_BULK_PAYMENT_DETAIL);
			preparedStatement.setInt(1, organizationId);
			preparedStatement.setString(2, transactionNo);
			logger.info("preparedStatement"+ preparedStatement.toString());
			rs = preparedStatement.executeQuery();
			BulkPaymentVo requestPayload = new BulkPaymentVo();

			while(rs.next()) {
				BulkTransactionDetailVo bulkPayment = new BulkTransactionDetailVo();
				bulkPayment.setTxnReference(rs.getString(1));
				bulkTransaction.setDebitAccount(rs.getString(2));
				bulkPayment.setTxnType(rs.getString(3));
				bulkPayment.setAmount(rs.getDouble(4));
				bulkPayment.setIfsc(rs.getString(5));
				bulkPayment.setBeneficiaryName(rs.getString(6));
				bulkPayment.setAcountNo(rs.getString(7));
				bulkPayment.setStatus(rs.getString(8));
				if(bulkPayment.getStatus() !=null && (bulkPayment.getStatus().equalsIgnoreCase("pending"))){
					bulkPayment.setStatus("Payment Initiated");
				}else{
					if(bulkPayment.getStatus() !=null && bulkPayment.getStatus().equalsIgnoreCase("failed")){
						bulkPayment.setStatus(CommonConstants.DISPLAY_STATUS_AS_FAILED);
					}
					if(bulkPayment.getStatus()!=null) {
						switch (bulkPayment.getStatus()) {
						case CommonConstants.STATUS_AS_REJECT:
							bulkPayment.setStatus(CommonConstants.DISPLAY_STATUS_AS_REJECT);
							break;

						case CommonConstants.STATUS_AS_DRAFT:
							bulkPayment.setStatus(CommonConstants.DISPLAY_STATUS_AS_DRAFT);
							break;
							
						case YesBankConstants.SettlementCompleted:
							bulkPayment.setStatus(YesBankConstants.SettlementCompleted);
							break;

						case YesBankConstants.SettlementReversed:
							bulkPayment.setStatus(YesBankConstants.SettlementReversed);
							break;
							
						}
						
					}
					
				}
				
				
				if(rs.getString(9)!=null && !rs.getString(9).isEmpty()) {
					requestPayload = objectMapper.readValue(rs.getString(9),
							BulkPaymentVo.class);
					//bulkPaymentsDetails.add(bulkPayment);
				}
				bulkTransaction.setCustomerId(rs.getString(10));
				String uiRequestJson = rs.getString(11);
				if(uiRequestJson!=null && !uiRequestJson.isEmpty()) {
					JSONParser parser = new JSONParser();
					JSONObject jsonData = (JSONObject) parser.parse(uiRequestJson);
					if(jsonData!=null) {
						if(jsonData.get("data")!=null) {
							logger.info("data:"+jsonData.get("data").toString());
							JSONObject data = (JSONObject) parser.parse(jsonData.get("data").toString());
							String paymentDescription = data.get("paymentDescription").toString();
							bulkTransaction.setPaymentDescription(paymentDescription);
							}
					}
				}
				logger.info("status"+bulkPayment.getStatus());

				bulkPaymentsDetails.add(bulkPayment);
			}
			logger.info("List of temp bulk payments ::"+bulkPaymentsDetails);
			Map <String , String > remarksMap = new HashMap<String, String>();
			if(requestPayload!=null && requestPayload.getData()!=null && requestPayload.getData().getDomesticPayments()!=null && requestPayload.getData().getDomesticPayments().size()>0 ) {
				for(BulkPaymentDomesticPaymentVo domesticPayment : requestPayload.getData().getDomesticPayments()) {
					remarksMap.putIfAbsent(domesticPayment.getInitiation().getInstructionIdentification(), domesticPayment.getInitiation().getRemittanceInformation().getUnstructured().getCreditorReferenceInformation());
				}
			}
				
			if(remarksMap!=null && !remarksMap.isEmpty()) {	
			for(BulkTransactionDetailVo bulkPayment : bulkPaymentsDetails) {
				bulkPayment.setNarration(remarksMap.get(bulkPayment.getTxnReference()));
			}
			}
			logger.info("List of temp bulk payments ::"+bulkPaymentsDetails.size());
			Double successSum = 0.00;
			Double failureSum = 0.00;

				for (BulkTransactionDetailVo payment:bulkPaymentsDetails  ) {
					if(YesBankConstants.SettlementCompleted.equalsIgnoreCase(payment.getStatus())) {
						successSum = successSum + payment.getAmount();
					}
					if(YesBankConstants.SettlementReversed.equalsIgnoreCase(payment.getStatus())) {
						failureSum = failureSum + payment.getAmount();
					}
				}
			if(bulkPaymentsDetails.size()>0) {
				bulkTransaction.setTxnIdentifier(transactionNo);
				bulkTransaction.setTransactionDetails(bulkPaymentsDetails);
				Double totalSum =  bulkPaymentsDetails.stream().mapToDouble(BulkTransactionDetailVo::getAmount).sum();
				bulkTransaction.setNoOfTxn(bulkPaymentsDetails.size());
				bulkTransaction.setTotalAmount(totalSum);
				bulkTransaction.setTotalSuccessAmount(successSum);
				bulkTransaction.setTotalFailedAmount(failureSum);
			}
			

			logger.info("bulkTransaction detail ::"+bulkTransaction);


		}
		catch (Exception ex) {
			logger.error("Exception while getting bulk payment information>>>" + ex);
			throw new ApplicationException(ex);
		} finally {
			closeResources(rs, preparedStatement, connection);
		}

		return bulkTransaction;


	}

	public Double getTotalAmountPaidAndReturned (String orgId , String fileIdentifier , String status) throws ApplicationException {
		Double sumAmount = 0.00;

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			connection = getBankingConnection();
		
			if(Objects.nonNull(fileIdentifier)) {
				preparedStatement =
						connection.prepareStatement(
								YesBankConstants.GET_TOTAL_AMOUNT);
				preparedStatement.setString(1, orgId);
				preparedStatement.setString(2, status);
				preparedStatement.setString(3,fileIdentifier);
				rs = preparedStatement.executeQuery();
				while(rs.next()) {
					sumAmount = rs.getDouble(1);
				}
				logger.info("Customer information return value>>>" +sumAmount);
			}


		} catch (Exception ex) {
			logger.error("Exception while saving payment information>>>" + ex);
			throw new ApplicationException(ex);
		} finally {
			closeResources(rs, preparedStatement, connection);
		}
		return sumAmount;
	}
	
	
	public void makeBulkPayment(String fileIdentifier,String paymentTransferJsonRes,String fileName) throws ApplicationException{
		
		logger.info("Entry into makeBulkPayment>>>" +fileIdentifier);
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			connection = getBankingConnection();
		
			if(Objects.nonNull(fileIdentifier)) {
				preparedStatement =connection.prepareStatement(YesBankConstants.UPDATE_BULK_PAYMENT);
				preparedStatement.setString(1, paymentTransferJsonRes);
				preparedStatement.setString(2, "PENDING");
				preparedStatement.setString(3,fileName);
				preparedStatement.setString(4,fileIdentifier);
				preparedStatement.executeUpdate();
			}


		} catch (Exception ex) {
			logger.error("Exception makeBulkPayment>>>" , ex);
			throw new ApplicationException(ex);
		} finally {
			closeResources(rs, preparedStatement, connection);
		}

	}

	
	private String getBulkPaymentAmount(String fileIdentifier) throws ApplicationException{
		
		logger.info("Entry into makeBulkPayment>>>" +fileIdentifier);
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String amount=null;
		try {
			connection = getBankingConnection();
		
			if(Objects.nonNull(fileIdentifier)) {
				preparedStatement =
						connection.prepareStatement(
								YesBankConstants.GET_BULK_PAYMENT_AMOUNT);
				preparedStatement.setString(1, fileIdentifier);
				rs=preparedStatement.executeQuery();
				while (rs.next()) {
					amount=rs.getString(1);
					
				}
			}


		} catch (Exception ex) {
			logger.error("Exception makeBulkPayment>>>" , ex);
			throw new ApplicationException(ex);
		} finally {
			closeResources(rs, preparedStatement, connection);
		}
		return amount;
	}	
	
	
	public int updateBankAccountSetting(YesBankCustomerInformationVo informationVo)
			throws ApplicationException {

		Connection con = null;
		PreparedStatement preparedStatement = null;

		try {
			con = getBankingConnection();
			// To update Bank account setting
			preparedStatement = con.prepareStatement(YesBankConstants.UPDATE_BANK_ACCOUNT_SETTING);
			preparedStatement.setString(1, informationVo.getAccountName());
			preparedStatement.setString(2, informationVo.getIfsc());
			preparedStatement.setString(3, informationVo.getMobileNo());
			preparedStatement.setBoolean(4, informationVo.getIsPaymentAllowed());
			preparedStatement.setInt(5, informationVo.getErpBankAccountId());
			preparedStatement.setString(6, "Bank Account");
			if(informationVo.getIsApiBankingEnabled()) {
				preparedStatement.setString(7, "Y");
			}else {
				preparedStatement.setString(7, "N");
			}
			preparedStatement.setInt(8, informationVo.getId());
			int rowAffected = preparedStatement.executeUpdate();
			logger.info("Customer information return value>>>" + rowAffected);
			return rowAffected;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Exception in saving the customer information>>>>" + ex);
			throw new ApplicationException(ex.getMessage());
		} finally {
			closeResources(null, preparedStatement, con);
		}
	}
}
