package com.blackstrawai.externalintegration.banking.yesbank;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.DateConverter;
import com.blackstrawai.common.FinanceCommonDao;
import com.blackstrawai.externalintegration.yesbank.WorkflowBankingVo;
import com.blackstrawai.externalintegration.yesbank.YesBankCustomerInformationVo;
import com.blackstrawai.externalintegration.yesbank.Request.bulkpayments.BulkPaymentDomesticPaymentVo;
import com.blackstrawai.externalintegration.yesbank.Request.bulkpayments.BulkPaymentVo;
import com.blackstrawai.externalintegration.yesbank.Request.bulkpayments.BulkPaymentsListVo;
import com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer.PaymentTransferVo;
import com.blackstrawai.externalintegration.yesbank.Response.YesBankAuthorizationVo;
import com.blackstrawai.externalintegration.yesbank.Response.bulkpayments.BulkPaymentsResponse;
import com.blackstrawai.externalintegration.yesbank.Response.bulkpayments.BulkTransactionVo;
import com.blackstrawai.externalintegration.yesbank.Response.common.RequestData;
import com.blackstrawai.externalintegration.yesbank.Response.fundconfirmation.FundsConfirmationResponseVo;
import com.blackstrawai.externalintegration.yesbank.Response.payments.CopyPaymentTransactionVo;
import com.blackstrawai.externalintegration.yesbank.Response.payments.PaymentAccountingInfoVo;
import com.blackstrawai.externalintegration.yesbank.Response.payments.PaymentDetailsVo;
import com.blackstrawai.externalintegration.yesbank.Response.payments.PaymentInfoVo;
import com.blackstrawai.externalintegration.yesbank.Response.payments.PaymentResponseVo;
import com.blackstrawai.externalintegration.yesbank.Response.payments.PaymentTransactionInfoVo;
import com.blackstrawai.externalintegration.yesbank.Response.payments.PaymentTransactionResponseVo;
import com.blackstrawai.externalintegration.yesbank.Response.payments.PaymentTransactionVo;
import com.blackstrawai.externalintegration.yesbank.Response.paymenttransfer.PaymentTransferDomesticPaymentsResponseVo;
import com.blackstrawai.externalintegration.yesbank.Response.paymenttransfer.PaymentTransferResponseVo;
import com.blackstrawai.externalintegration.yesbank.beneficiary.BeneficiaryTransactionResponseVo;
import com.blackstrawai.externalintegration.yesbank.dropdowns.DropDownVo;
import com.blackstrawai.externalintegration.yesbank.dropdowns.PaymentsDropDownVo;
import com.blackstrawai.settings.SettingsModuleOrganizationDao;
import com.blackstrawai.settings.SettingsModuleOrganizationVo;
import com.blackstrawai.workflow.WorkflowConstants;
import com.blackstrawai.workflow.WorkflowProcessHelperService;
import com.blackstrawai.workflow.WorkflowProcessService;
import com.blackstrawai.workflow.WorkflowSettingsUserApprovalDao;
import com.blackstrawai.workflow.WorkflowSettingsVo;
import com.blackstrawai.workflow.WorkflowThread;
import com.blackstrawai.yesbank.YesBank;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class YesBankPaymentsService {

	private final ObjectMapper objectMapper = new ObjectMapper();
	private final Logger logger = Logger.getLogger(YesBankPaymentsService.class);

	private final YesBankIntegrationDao yesBankIntegrationDao;
	private final YesBankBeneficiaryDao yesBankBeneficiaryDao;
	private final WorkflowProcessHelperService workflowProcessHelperService;
	private final WorkflowProcessService workflowProcessService;
	private final SettingsModuleOrganizationDao settingsModuleOrganizationDao;
	private final YesBankPaymentHelperService yesBankPaymentHelperService;

	public YesBankPaymentsService(YesBankIntegrationDao yesBankIntegrationDao,
			YesBankBeneficiaryDao yesBankBeneficiaryDao, YesBankPaymentHelperService yesBankPaymentHelperService,
			WorkflowProcessHelperService workflowProcessHelperService, WorkflowProcessService workflowProcessService,
			SettingsModuleOrganizationDao settingsModuleOrganizationDao,
			WorkflowSettingsUserApprovalDao workflowSettingsUserApprovalDao, FinanceCommonDao financeCommonDao) {
		this.yesBankIntegrationDao = yesBankIntegrationDao;
		this.yesBankBeneficiaryDao = yesBankBeneficiaryDao;
		this.workflowProcessHelperService=workflowProcessHelperService;
		this.workflowProcessService=workflowProcessService;
		this.settingsModuleOrganizationDao=settingsModuleOrganizationDao;
		this.yesBankPaymentHelperService=yesBankPaymentHelperService;
	}

	public PaymentTransferResponseVo paymentTransfer(
			PaymentTransferVo paymentTransferPaymentsVo,
			String organizationId,
			String userId,
			String roleName,
			CopyPaymentTransactionVo paymentTransactionVo)
					throws ApplicationException, IOException {

		try {
			String finalJsonRequest = objectMapper.writeValueAsString(paymentTransferPaymentsVo);
			String uiJsonRequest = objectMapper.writeValueAsString(paymentTransactionVo);
			int paymentId=yesBankIntegrationDao.savePaymentTransferInfo(
					paymentTransferPaymentsVo,
					finalJsonRequest,
					null,
					null,
					organizationId,
					userId,
					roleName,
					uiJsonRequest,true);
			PaymentTransferResponseVo paymentTransferResponseVo=null;
			if (paymentId > 0) {// If worfkflow enabled
				int orgId = Integer.parseInt(organizationId != null ? organizationId : "0");
				// Get Settings for workflow
				SettingsModuleOrganizationVo settingsModuleVo = settingsModuleOrganizationDao.getSettingsModuleOrganizationBySubmodule(orgId, WorkflowConstants.MODULE_BANKING);
				if (settingsModuleVo != null && settingsModuleVo.isRequired()) {
					List<WorkflowSettingsVo> applicableRules = workflowProcessHelperService.getApplicableRulesForModuleEntity(orgId, paymentId, WorkflowConstants.MODULE_BANKING);
					if (applicableRules != null && !applicableRules.isEmpty()) {
						paymentTransferResponseVo=new PaymentTransferResponseVo();
						// Triggering Workflow
						new WorkflowThread(WorkflowConstants.MODULE_BANKING, paymentId, workflowProcessService,applicableRules, WorkflowConstants.WORKFLOW_OPERATION_CREATE).start();
						Thread.sleep(1000);
					} else {

						paymentTransferResponseVo=yesBankPaymentHelperService.makeSinglePayment(paymentTransferPaymentsVo, organizationId, userId, roleName, finalJsonRequest,
								uiJsonRequest, paymentId);
					}
				} else {
					paymentTransferResponseVo=yesBankPaymentHelperService.makeSinglePayment(paymentTransferPaymentsVo, organizationId, userId, roleName, finalJsonRequest,
							uiJsonRequest, paymentId);
				}

			} else {

				paymentTransferResponseVo=yesBankPaymentHelperService.makeSinglePayment(paymentTransferPaymentsVo, organizationId, userId, roleName, finalJsonRequest,
						uiJsonRequest, paymentId);
			}
			return paymentTransferResponseVo;
		} catch (Exception ex) {
			throw new ApplicationException(ex);
		}
	}



	@SuppressWarnings("rawtypes")
	public FundsConfirmationResponseVo fundsConfirmation(
			String orgId,
			String customerId,
			String accountNumber,
			RequestData requestData,
			String userId,
			String roleName)
					throws ApplicationException, IOException {

		try {
			String jsonRequest = objectMapper.writeValueAsString(requestData);
			logger.info("Final Payment Transfer Request" + jsonRequest);

			YesBankAuthorizationVo authorization =
					yesBankIntegrationDao.getCustomerAuthorization(orgId, customerId, accountNumber,userId,roleName);

			if (Objects.nonNull(authorization)) {
				String fundsConfirmationJsonRes =
						YesBank.getInstance()
						.getSingleResponseEntity("fundsConfirmation", jsonRequest, authorization);

				JsonNode node = objectMapper.readTree(fundsConfirmationJsonRes);
				logger.info("node:::" + node);
				JsonNode code = node.get("Code");
				logger.info("Code:::" + code);

				FundsConfirmationResponseVo fundsConfirmationResponseVo = null;
				if (Objects.isNull(code)) {
					fundsConfirmationResponseVo =
							objectMapper.readValue(fundsConfirmationJsonRes, FundsConfirmationResponseVo.class);

					logger.info("<<<<Funds Confirmation Response>>>>" + fundsConfirmationResponseVo);
				}

				return fundsConfirmationResponseVo;

			} else {
				throw new ApplicationException(YesBankConstants.ERROR_YBL_CUST_INFO_NOT_AVAIALBLE);
			}

		} catch (Exception ex) {
			throw ex;
		}
	}

	public PaymentResponseVo getPaymentTransactionList(
			String orgId, String accountNumber, String beneficiaryId, String search, String roleName,String userId)
					throws ApplicationException, IOException, ParseException {

		PaymentResponseVo paymentResponseVo = null;
		try {

			List<BeneficiaryTransactionResponseVo> transactionList =
					yesBankIntegrationDao.getPaymentTransactionList(
							orgId, accountNumber, beneficiaryId, search, roleName,userId);

			if (transactionList.size() > 0) {
				paymentResponseVo = getPaymentTransactions(transactionList);
			}

		} catch (Exception ex) {
			logger.error("Error in getPaymentTransactionList",ex);
			throw ex;
		}
		return paymentResponseVo;
	}

	public PaymentResponseVo getPaymentTransactions(
			List<BeneficiaryTransactionResponseVo> transactionList) throws IOException, ParseException {

		BeneficiaryTransactionResponseVo beneficiaryTransactionResponseVo;
		PaymentResponseVo paymentResponseVo = new PaymentResponseVo();
		List<BeneficiaryTransactionResponseVo> paymentResponseList = new ArrayList<>();
		for (BeneficiaryTransactionResponseVo paymentTransaction : transactionList) {
			beneficiaryTransactionResponseVo = new BeneficiaryTransactionResponseVo();
			PaymentTransferResponseVo paymentTransactionResponse =null;
			PaymentTransferDomesticPaymentsResponseVo data=null;
			String transactionDate=null;

			if(paymentTransaction!=null && paymentTransaction.getResponseJson()!=null){

				paymentTransactionResponse =
						objectMapper.readValue(
								paymentTransaction.getResponseJson(), PaymentTransferResponseVo.class);

				data = paymentTransactionResponse.getData();

				transactionDate =  data.getCreationDateTime()!=null && ! data.getCreationDateTime().isEmpty() ? 
						DateConverter.getInstance()
						.convertTimestampToDate(
								"yyyy-MM-dd'T'HH:mm:ss", "dd/MM/yyyy", data.getCreationDateTime()) : null;
			}
			//    	if(transactionDate==null) {
			//        transactionDate =
			//                DateConverter.getInstance()
			//                    .convertTimestampToDate(
			//                        "yyyy-MM-dd'T'HH:mm:ss", "dd/MM/yyyy", paymentTransaction.getTransactionDate());
			//    	}
			beneficiaryTransactionResponseVo.setId(paymentTransaction.getId());
			beneficiaryTransactionResponseVo.setTransactionDate(transactionDate!=null?transactionDate:paymentTransaction.getTransactionDate());
			beneficiaryTransactionResponseVo.setTransactionNumber(
					data!=null && data.getInitiation()!=null && data.getInitiation().getInstructionIdentification()!=null?data.getInitiation().getInstructionIdentification():paymentTransaction.getTransactionNumber());
			beneficiaryTransactionResponseVo.setBeneficiaryName(
					data!=null  && data.getInitiation()!=null &&data.getInitiation().getCreditorAccount()!=null && data.getInitiation().getCreditorAccount().getSchemeName()!=null?data.getInitiation().getCreditorAccount().getSchemeName():paymentTransaction.getBeneficiaryName());
			beneficiaryTransactionResponseVo.setBeneficiaryType(paymentTransaction.getBeneficiaryType());
			beneficiaryTransactionResponseVo.setAmount(paymentTransaction.getAmount());
			beneficiaryTransactionResponseVo.setStatus(paymentTransaction.getStatus());

			paymentResponseList.add(beneficiaryTransactionResponseVo);
			paymentResponseVo.setPaymentTransaction(paymentResponseList);
		}
		return paymentResponseVo;
	}

	public PaymentsDropDownVo getPaymentDropDownList(int organizationId) throws ApplicationException {

		PaymentsDropDownVo paymentsDropDownVo = new PaymentsDropDownVo();

		paymentsDropDownVo.setBeneficiaryDropDown(
				yesBankBeneficiaryDao.getPaymentDropDownValues(organizationId));

		List<DropDownVo> dropDownList = new ArrayList<>();
		DropDownVo dropDownVo;

		for (Map.Entry<Integer, String> paymentModeDrop :
			YesBankConstants.paymentModeMapping.entrySet()) {
			dropDownVo = new DropDownVo();
			dropDownVo.setId(paymentModeDrop.getKey());
			dropDownVo.setName(paymentModeDrop.getValue());
			dropDownVo.setValue(paymentModeDrop.getValue());
			dropDownList.add(dropDownVo);
		}

		paymentsDropDownVo.setPaymentsDropDown(dropDownList);
		return paymentsDropDownVo;
	}

	public PaymentTransactionResponseVo viewPaymentTransaction(
			String organizationId, String transactionId)
					throws ApplicationException, IOException, ParseException {

		PaymentTransactionVo paymentTransactionVo =
				yesBankIntegrationDao.paymentTransaction(organizationId, transactionId);

		PaymentTransactionResponseVo paymentTransactionResponseVo = new PaymentTransactionResponseVo();

		if (Objects.nonNull(paymentTransactionVo.getTransactionId())) {

			CopyPaymentTransactionVo copyPaymentTransactionVo =
					objectMapper.readValue(
							paymentTransactionVo.getUiRequestJson(), CopyPaymentTransactionVo.class);

			PaymentTransactionInfoVo paymentTransactionInfoVo = new PaymentTransactionInfoVo();
			paymentTransactionInfoVo.setTransactionId(paymentTransactionVo.getTransactionId());

			String transactionDate =
					DateConverter.getInstance()
					.convertTimestampToDate(
							"yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy", paymentTransactionVo.getTransactionDate());

			paymentTransactionInfoVo.setTransactionDate(transactionDate);
			paymentTransactionInfoVo.setBankReferenceNo(paymentTransactionVo.getBankReferenceNo());
			paymentTransactionInfoVo.setStatus(paymentTransactionVo.getStatus());
			paymentTransactionResponseVo.setPaymentTransactions(paymentTransactionInfoVo);

			PaymentInfoVo paymentInfoVo = new PaymentInfoVo();

			PaymentDetailsVo paymentDetailsVo = copyPaymentTransactionVo.getSinglePayment();

			paymentInfoVo.setPayingTo(paymentDetailsVo.getPaidTo());
			paymentInfoVo.setBeneficiaryName(paymentDetailsVo.getBeneficiaryName());
			paymentInfoVo.setBankAccountName(paymentDetailsVo.getBankAccountName());
			paymentInfoVo.setAccountNo(paymentDetailsVo.getBeneficiaryAccountNumber());
			paymentInfoVo.setIFSCCode(paymentDetailsVo.getIfscCode());
			paymentInfoVo.setAmount(Double.parseDouble(paymentDetailsVo.getAmount()));
			paymentInfoVo.setPaymentMode(paymentDetailsVo.getPaymentMode());
			paymentTransactionResponseVo.setPaymentInformation(paymentInfoVo);

			PaymentAccountingInfoVo paymentAccountingInfoVo = new PaymentAccountingInfoVo();
			paymentAccountingInfoVo.setBillReference(paymentDetailsVo.getBillRef());
			paymentAccountingInfoVo.setPaymentReference(paymentDetailsVo.getPaymentRef());

			paymentTransactionResponseVo.setPaymentAccountingInformation(paymentAccountingInfoVo);
		}

		return paymentTransactionResponseVo;
	}

	public CopyPaymentTransactionVo copyPaymentTransaction(
			String organizationId, String transactionId) throws ApplicationException, IOException {

		PaymentTransactionVo paymentTransactionVo =yesBankIntegrationDao.paymentTransaction(organizationId, transactionId);
		if (Objects.isNull(paymentTransactionVo.getTransactionId())) {
			throw new ApplicationException(YesBankConstants.ERROR_YBL_PAYMENT_NOT_AVAILABLE);
		}
		CopyPaymentTransactionVo copyPaymentTransactionVo =objectMapper.readValue(paymentTransactionVo.getUiRequestJson(), CopyPaymentTransactionVo.class);
		return copyPaymentTransactionVo;
	}

	public WorkflowBankingVo getWorfklowDataForPayment(int paymentId) throws ApplicationException {
		return yesBankIntegrationDao.getWorfklowDataForPayment(paymentId);
	}




	public BulkPaymentsResponse bulkPaymentTransfer(BulkPaymentVo bulkPaymentVo, int orgId, String userId,String roleName ,String uiReqJson ,YesBankCustomerInformationVo debtorDetails) {
		BulkPaymentsResponse paymentTransferResponseVo=new BulkPaymentsResponse();
		
		try {
			String finalJsonRequest = objectMapper.writeValueAsString(bulkPaymentVo);
			int paymentId=0;
			for(BulkPaymentDomesticPaymentVo paymentDetail: bulkPaymentVo.getData().getDomesticPayments()) {
				paymentId=yesBankIntegrationDao.saveBulkPaymentTransferInfo(
						bulkPaymentVo.getData(),
						finalJsonRequest,
						paymentTransferResponseVo,
						null,
						Integer.valueOf(orgId),
						userId,
						roleName,
						uiReqJson,true ,debtorDetails,paymentDetail);
			}
			logger.info("Payment Id:"+paymentId);
			if (paymentId > 0) {// If worfkflow enabled
			//	int orgId = Integer.parseInt(organizationId != null ? organizationId : "0");
				// Get Settings for workflow
				SettingsModuleOrganizationVo settingsModuleVo = settingsModuleOrganizationDao.getSettingsModuleOrganizationBySubmodule(orgId, WorkflowConstants.MODULE_BANKING);
				if (settingsModuleVo != null ) {
					if( settingsModuleVo.isRequired()) {
					List<WorkflowSettingsVo> applicableRules = workflowProcessHelperService.getApplicableRulesForModuleEntity(orgId, paymentId, WorkflowConstants.MODULE_BANKING);
					if (applicableRules != null) {
						if(!applicableRules.isEmpty()) {
						paymentTransferResponseVo=new BulkPaymentsResponse();
						// Triggering Workflow
						new WorkflowThread(WorkflowConstants.MODULE_BANKING, paymentId, workflowProcessService,applicableRules, WorkflowConstants.WORKFLOW_OPERATION_CREATE).start();
						Thread.sleep(1000);
					} else {
					
						logger.info("applicableRules :"+applicableRules);
						paymentTransferResponseVo=yesBankPaymentHelperService.makeBulkPayment(paymentId);
					}
					}
				} else {
					logger.info("settingsModuleVo :"+settingsModuleVo);
					
					paymentTransferResponseVo=yesBankPaymentHelperService.makeBulkPayment(paymentId);
					}
				}

			} 

		} catch (Exception ex) {
			logger.info("error::" + ex);
		}
		return paymentTransferResponseVo;
	}

	public List<BulkPaymentsListVo> getBulkPaymentTransferList( Integer orgId, Integer userId,String roleName , String accountNo) throws ApplicationException {

		try {
			List<BulkPaymentsListVo> bulkpayments= yesBankIntegrationDao.getBulkPaymentsList(orgId , userId,roleName , accountNo);


			return bulkpayments;
		} catch (Exception ex) {
			throw new ApplicationException(ex);
		}
	}

	public BulkTransactionVo viewBulkTransactionDetails(Integer organizationId, String roleName, String userId,String transactionNo) throws ApplicationException {
		BulkTransactionVo bulkTxnDetails = yesBankIntegrationDao.getBulkTransactionDetails (organizationId,roleName,userId,transactionNo);


		return bulkTxnDetails;
	}

	public Map<String , String > getBulkTxnPaymentStatus(String fileIdentifier ,String orgId ,String uniqueIdentifier,String customerId, String accountNo,String userId, String roleName) throws ApplicationException {

		Map<String , String > resultMap = new HashMap<String, String>();
		String status = yesBankPaymentHelperService.getSingleTxnStatusOfBulkPayment(fileIdentifier,orgId, uniqueIdentifier, customerId, accountNo, userId, roleName);
		Double successAmnt = yesBankIntegrationDao.getTotalAmountPaidAndReturned(orgId, fileIdentifier, YesBankConstants.SettlementCompleted);
		Double returnedAmnt = yesBankIntegrationDao.getTotalAmountPaidAndReturned(orgId, fileIdentifier, YesBankConstants.SettlementReversed);
		resultMap.put("Status", status);		
		resultMap.put("successAmnt", String.valueOf(successAmnt));
		resultMap.put("returnedAmnt", String.valueOf(returnedAmnt));
		return resultMap ;


	}


}
