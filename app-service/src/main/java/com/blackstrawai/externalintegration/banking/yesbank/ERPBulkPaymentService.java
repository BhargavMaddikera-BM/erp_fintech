package com.blackstrawai.externalintegration.banking.yesbank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.DateConverter;
import com.blackstrawai.ap.BillsInvoiceDao;
import com.blackstrawai.ap.PaymentNonCoreConstants;
import com.blackstrawai.ap.dropdowns.PaymentNonCoreBillDetailsDropDownVo;
import com.blackstrawai.ap.payment.noncore.PaymentNonCoreBaseVo;
import com.blackstrawai.ap.payment.noncore.PaymentNonCoreCustomTableVo;
import com.blackstrawai.ap.payment.noncore.PaymentNonCoreVo;
import com.blackstrawai.banking.BankMasterDao;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.externalintegration.yesbank.YesBankCustomerInformationVo;
import com.blackstrawai.externalintegration.yesbank.Request.bulkpayments.BulkPaymentDomesticPaymentVo;
import com.blackstrawai.externalintegration.yesbank.Request.bulkpayments.BulkPaymentVo;
import com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer.PaymentTransferVo;
import com.blackstrawai.externalintegration.yesbank.Response.YesBankAuthorizationVo;
import com.blackstrawai.externalintegration.yesbank.Response.bulkpayments.BulkPaymentsResponse;
import com.blackstrawai.externalintegration.yesbank.Response.payments.ErpSinglePaymentTransactionVo;
import com.blackstrawai.externalintegration.yesbank.Response.paymenttransfer.PaymentTransferResponseVo;
import com.blackstrawai.externalintegration.yesbank.beneficiary.BeneficiaryTypeVo;
import com.blackstrawai.externalintegration.yesbank.dropdowns.APEmployeesPayRunDropDownVo;
import com.blackstrawai.externalintegration.yesbank.dropdowns.APVendorBillsDropDownVo;
import com.blackstrawai.externalintegration.yesbank.dropdowns.BeneficiaryDropDownVo;
import com.blackstrawai.externalintegration.yesbank.dropdowns.PaymentBeneficiaryListVo;
import com.blackstrawai.externalintegration.yesbank.erpbulkpayments.BaseERPPaymentVo;
import com.blackstrawai.externalintegration.yesbank.erpbulkpayments.ERPPaymentDetailVo;
import com.blackstrawai.journals.GeneralLedgerService;
import com.blackstrawai.journals.GeneralLedgerVo;
import com.blackstrawai.journals.JournalEntriesConstants;
import com.blackstrawai.settings.CurrencyDao;
import com.blackstrawai.settings.CurrencyVo;
<<<<<<< HEAD
import com.blackstrawai.settings.SettingsModuleOrganizationDao;
=======
>>>>>>> 0a694d861a9a446c4b7c10a39cf5c7647a3014bd
import com.blackstrawai.settings.SettingsModuleOrganizationVo;
import com.blackstrawai.workflow.WorkflowConstants;
import com.blackstrawai.workflow.WorkflowProcessHelperService;
import com.blackstrawai.workflow.WorkflowProcessService;
import com.blackstrawai.workflow.WorkflowSettingsVo;
import com.blackstrawai.workflow.WorkflowThread;
import com.blackstrawai.yesbank.YesBank;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ERPBulkPaymentService extends BaseService{

	private Logger logger = Logger.getLogger(ERPBulkPaymentService.class);
	
	private final ObjectMapper objectMapper = new ObjectMapper();


	@Autowired
	private ERPBulkPaymentsDao bulkPaymentsDao;
	
	@Autowired
	private YesBankIntegrationDao yesBankIntegrationDao;
	
	@Autowired 
	private YesBankPaymentHelperService  yesBankPaymentHelperService;
	
	@Autowired
	private BankMasterDao bankMasterDao;
	
	@Autowired
	private CurrencyDao currencyDao;
	
	@Autowired
	private BillsInvoiceDao billsInvoiceDao;
	
	@Autowired
	private GeneralLedgerService generalLedgerService;
	
<<<<<<< HEAD
	@Autowired
private WorkflowProcessHelperService workflowProcessHelperService;

@Autowired
private WorkflowProcessService workflowProcessService;

@Autowired
private SettingsModuleOrganizationDao settingsModuleOrganizationDao;

	
=======
>>>>>>> 0a694d861a9a446c4b7c10a39cf5c7647a3014bd
	public List<APVendorBillsDropDownVo> getVendorsBillsAndBankDetails(Integer orgId , String roleName, String userId,String currencyId) throws ApplicationException {
		return bulkPaymentsDao.getVendorsBillsAndBankDetails(orgId, roleName, userId , currencyId);
	}
	
<<<<<<< HEAD
	public List<APEmployeesPayRunDropDownVo> getEmployeePayRunAndBankDetails(Integer orgId , String roleName, String userId,String currencyId) throws ApplicationException {
		return bulkPaymentsDao.getEmployeePayRunAndBankDetails(orgId, roleName, userId , currencyId);
	}
	
=======
>>>>>>> 0a694d861a9a446c4b7c10a39cf5c7647a3014bd
	public BeneficiaryDropDownVo getERPAccountDropDownList(int organizationId) throws ApplicationException {
		BeneficiaryDropDownVo dropDownVo =  new BeneficiaryDropDownVo();
		List<BeneficiaryTypeVo> beneficiaryTypes = new ArrayList<BeneficiaryTypeVo>();
		BeneficiaryTypeVo beneficiaryTypeVo = new BeneficiaryTypeVo();
		List<PaymentBeneficiaryListVo> list = bulkPaymentsDao.getERPBankingAccountDetails(organizationId);
		beneficiaryTypeVo.setBeneficiaryList(list);
		beneficiaryTypes.add(beneficiaryTypeVo);
		dropDownVo.setBeneficiaryTypes(beneficiaryTypes);
		logger.info("beneficiaryTypes"+beneficiaryTypes);
		return dropDownVo;
	}


	public BulkPaymentsResponse bulkPaymentTransfer(BulkPaymentVo bulkPaymentVo, BaseERPPaymentVo erpPaymentVo,
			YesBankCustomerInformationVo debtorDetails) {

		ObjectMapper objectMapper = new ObjectMapper();
		BulkPaymentsResponse paymentTransferResponseVo=new BulkPaymentsResponse();
		
		try {
	  	    String uiReqJson = objectMapper.writeValueAsString(erpPaymentVo);
			String finalJsonRequest = objectMapper.writeValueAsString(bulkPaymentVo);
			int paymentId=0;
			
			for(BulkPaymentDomesticPaymentVo paymentDetail: bulkPaymentVo.getData().getDomesticPayments()) {
					paymentId=yesBankIntegrationDao.saveBulkPaymentTransferInfo(
						bulkPaymentVo.getData(),
						finalJsonRequest,
						paymentTransferResponseVo,
						null,
						erpPaymentVo.getOrgId(),
						erpPaymentVo.getUserId(),
						erpPaymentVo.getRoleName(),
						uiReqJson,true ,debtorDetails,paymentDetail);
					
			}
			logger.info("Payment Id:"+paymentId);
			
			//To insert into ERP payemnts table 
			for(ERPPaymentDetailVo detail : erpPaymentVo.getPaymentDetails()) {
				if(Objects.nonNull(detail)) {
					switch(erpPaymentVo.getModuleName()) {
					case YesBankConstants.MODULE_INVOICE_PAYMENT:
						detail.setKeyContactType(YesBankConstants.KEYCONTACT_VENDOR);
						break;
					case YesBankConstants.MODULE_PO_PAYMENT:
						detail.setKeyContactType(YesBankConstants.KEYCONTACT_VENDOR);
						break;
					case YesBankConstants.MODULE_PAYRUN_PAYMENT:
						detail.setKeyContactType(YesBankConstants.KEYCONTACT_EMPLOYEE);
						break;
					}
				}
			bulkPaymentsDao.insertIntoErpBulkPayment(detail ,erpPaymentVo);
			}
			/*	if (paymentId > 0) {// If worfkflow enabled
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
					*/
					
					paymentTransferResponseVo=yesBankPaymentHelperService.makeBulkPayment(paymentId);


		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info("error::" + ex);
		}
		return paymentTransferResponseVo;
	
	}
	public List<APVendorBillsDropDownVo> getPurchaseOrderAndBankDetails(Integer orgId , String roleName, String userId,String currencyId) throws ApplicationException {
		return bulkPaymentsDao.getPurchaseOrderAndBankDetails(orgId, roleName, userId , currencyId);
	}


	public BaseERPPaymentVo getGeneralLedger(BaseERPPaymentVo erpPaymentVo, YesBankCustomerInformationVo debtorDetails) throws ApplicationException {
		BaseERPPaymentVo erp = null;
		if(Objects.nonNull(erpPaymentVo.getModuleName())) {
			switch(erpPaymentVo.getModuleName()) {
			case YesBankConstants.MODULE_INVOICE_PAYMENT:
				logger.info("To get GL for invoice Payment");
				 erp = getERPPaymentsForInvoice(erpPaymentVo ,debtorDetails);
				break;
			case YesBankConstants.MODULE_VENDOR_PAYMENT:
				logger.info("To get GL for vendor Payment");
				 erp = getERPPaymentsForInvoice(erpPaymentVo ,debtorDetails);
				break;
			case YesBankConstants.MODULE_PO_PAYMENT:
			//	getERPPaymentsForPo(erpPaymentVo);
				break;
			case YesBankConstants.MODULE_PAYRUN_PAYMENT:
			//	getERPPaymentsForPayrun(erpPaymentVo);
				break;
			}
		
		
	}
		return erp;

}

	private BaseERPPaymentVo getERPPaymentsForInvoice(BaseERPPaymentVo erpPaymentVo, YesBankCustomerInformationVo debtorDetails) throws ApplicationException {
		if(erpPaymentVo.getPaymentDetails()!=null) {
			 Map<Integer,List<ERPPaymentDetailVo>> vendorGroupedPayments =erpPaymentVo.getPaymentDetails().stream()
                     .collect(Collectors.groupingBy(ERPPaymentDetailVo::getKeyContactId));
			 logger.info("vendorGroupedPayments::"+vendorGroupedPayments);
			 if(vendorGroupedPayments!=null && !vendorGroupedPayments.isEmpty() && erpPaymentVo.getDebitAccountId()!=null) {
				 int counter = 0;
				 List<PaymentNonCoreVo> list = new ArrayList<PaymentNonCoreVo>();
				  for (Map.Entry<Integer,List<ERPPaymentDetailVo>> entry : vendorGroupedPayments.entrySet())  {
			            logger.info("Key = " + entry.getKey() + ", Value = " + entry.getValue());  
				  		Integer vendorId = entry.getKey();
				  		//for(ERPPaymentDetailVo apPayment : entry.getValue() ) {
				  		list.addAll(convertToBillPaymentsType( entry.getValue() , vendorId , erpPaymentVo,debtorDetails,counter));
				  		counter++;
				  		logger.info("counter::"+counter);
				  		//}
				  }
				  logger.info("list::"+list.size());
				  erpPaymentVo.setGeneralLedgers(list);
			 }
			 
		}
		return erpPaymentVo;
	
	}

	private List<PaymentNonCoreVo> convertToBillPaymentsType(List<ERPPaymentDetailVo> apPaymentList, Integer vendorId,BaseERPPaymentVo erpPaymentVo,YesBankCustomerInformationVo debtorDetails, Integer counter) throws ApplicationException {
		List<PaymentNonCoreVo> glList = new ArrayList<PaymentNonCoreVo>();
		try {
		PaymentNonCoreVo apPaymentVo = new PaymentNonCoreVo();
		apPaymentVo.setOrganizationId(erpPaymentVo.getOrgId());
		apPaymentVo.setStatus(CommonConstants.STATUS_AS_ACTIVE);
		apPaymentVo.setRoleName(erpPaymentVo.getRoleName());
		apPaymentVo.setPaymentMode(2);
		apPaymentVo.setPaidVia(debtorDetails.getErpBankAccountId());
		apPaymentVo.setPaidViaName(bankMasterDao.getAccountName(debtorDetails.getErpBankAccountId(), "Bank Account"));
		apPaymentVo.setPaymentType(1);
		apPaymentVo.setPaymentRefNo(erpPaymentVo.getVoucherNo()+ (counter!=0 ? "-"+counter : ""));
		apPaymentVo.setPaymentDate(DateConverter.getInstance().convertDateToGivenFormat(erpPaymentVo.getTransactionDate(), "yyyy-MM-dd"));
		apPaymentVo.setVendor(vendorId);
		CurrencyVo currency = currencyDao.getCurrency(erpPaymentVo.getCurrencyId());
		apPaymentVo.setCurrency(erpPaymentVo.getCurrencyId());
		apPaymentVo.setCurrencySymbol(currency.getSymbol());
		apPaymentVo.setCurrencyCode(currency.getAlternateSymbol());
		apPaymentVo.setBaseCurrencyCode(currency.getAlternateSymbol());
		apPaymentVo.setNotes("");
		Double totalAmount = 0.00;
		List<PaymentNonCoreBaseVo> paymentList = new ArrayList<PaymentNonCoreBaseVo>();
		for(ERPPaymentDetailVo apPayment : apPaymentList) {
			PaymentNonCoreBaseVo billPayment = new PaymentNonCoreBaseVo();
			billPayment.setStatus(CommonConstants.STATUS_AS_ACTIVE);
			billPayment.setType(PaymentNonCoreConstants.BILL_PAYMENTS_TYPE_BILL);
			billPayment.setBillRef(apPayment.getReferenceModuleId()!=null ? String.valueOf(apPayment.getReferenceModuleId()) : "");
			PaymentNonCoreBillDetailsDropDownVo invoiceDetails = billsInvoiceDao.getBalanceDueByInvoiceId(apPayment.getReferenceModuleId(), erpPaymentVo.getOrgId(), erpPaymentVo.getCurrencyId(), vendorId, "dd/MM/yyyy");
			billPayment.setDueDate(invoiceDetails.getDueDate());
			billPayment.setTotalAmount(apPayment.getAmount());
			billPayment.setDueAmount(invoiceDetails.getBalanceDue()!=null ? String.valueOf(invoiceDetails.getBalanceDue()) : "");
			billPayment.setBillAmount(apPayment.getAmount());
			Double amt =  apPayment.getAmount()!=null ? Double.valueOf( apPayment.getAmount()) : 0.00;
			totalAmount = totalAmount + amt;
			paymentList.add(billPayment);
			apPaymentVo.setBulk(true);
		}
		logger.info("totalAmnt ::" + totalAmount);
		logger.info("paymentList ::" + paymentList.size());
		apPaymentVo.setTotalAmount(String.valueOf(totalAmount));
		apPaymentVo.setOthers1("");
		apPaymentVo.setOthers2("");
		apPaymentVo.setAmountPaid(String.valueOf(totalAmount));
		apPaymentVo.setBillRef(apPaymentList.size()>0 ? apPaymentList.get(0).getReferenceModuleId() : 0);
		apPaymentVo.setExchangeRate("1.00");
		List<PaymentNonCoreCustomTableVo> customTableList =  new ArrayList<PaymentNonCoreCustomTableVo>();

		PaymentNonCoreCustomTableVo billRef = new PaymentNonCoreCustomTableVo();
		billRef.setLedgerId("");
		billRef.setcName("billRef");
		billRef.setColName("Reference No");
		billRef.setLedgerName("");
		billRef.setColumnShow(true);
		customTableList.add(billRef);
		
		PaymentNonCoreCustomTableVo dueDate = new PaymentNonCoreCustomTableVo();
		dueDate.setLedgerId("");
		dueDate.setcName("dueDate");
		dueDate.setColName("Due Date");
		dueDate.setLedgerName("");
		dueDate.setColumnShow(true);
		customTableList.add(dueDate);

		PaymentNonCoreCustomTableVo others1 = new PaymentNonCoreCustomTableVo();
		others1.setLedgerId("");
		others1.setcName("others1");
		others1.setLedgerName("");
		others1.setColumnShow(false);
		customTableList.add(others1);

		PaymentNonCoreCustomTableVo others2 = new PaymentNonCoreCustomTableVo();
		others2.setLedgerId("");
		others2.setcName("others2");
		others2.setLedgerName("");
		others2.setColumnShow(false);
		customTableList.add(others2);

		apPaymentVo.setCustomTableList(customTableList);
		apPaymentVo.setPayments(paymentList);
		GeneralLedgerVo generalLedger = generalLedgerService.getGenealLedgers( JournalEntriesConstants.SUB_MODULE_PAYMENTS, apPaymentVo);
		apPaymentVo.setGeneralLedgerData(generalLedger);
		glList.add(apPaymentVo);
	/*	apPaymentList.forEach(value -> {
			if(value.getKeyContactId().equals(vendorId)) {
				value.setErpPaymentDetails(apPaymentVo);
			}
		});*/
		} catch (ApplicationException e) {
			logger.info("Exception in getting computning GL::"+e);
			throw new ApplicationException(e.getMessage());
		}
		return glList;
	}
<<<<<<< HEAD
	
	public PaymentTransferResponseVo paymentTransfer(
			PaymentTransferVo paymentTransferPaymentsVo,
			String organizationId,
			String userId,
			String roleName,
			ErpSinglePaymentTransactionVo paymentTransactionVo)
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

						paymentTransferResponseVo=makeSinglePayment(paymentTransferPaymentsVo, organizationId, userId, roleName, finalJsonRequest,
								uiJsonRequest, paymentId,paymentTransactionVo);
					}
				} else {
					paymentTransferResponseVo=makeSinglePayment(paymentTransferPaymentsVo, organizationId, userId, roleName, finalJsonRequest,
							uiJsonRequest, paymentId,paymentTransactionVo);
				}

			} else {

				paymentTransferResponseVo=makeSinglePayment(paymentTransferPaymentsVo, organizationId, userId, roleName, finalJsonRequest,
						uiJsonRequest, paymentId,paymentTransactionVo);
			}
			return paymentTransferResponseVo;
		} catch (Exception ex) {
			throw new ApplicationException(ex);
		}
	}

	public PaymentTransferResponseVo makeSinglePayment(PaymentTransferVo paymentTransferPaymentsVo, String organizationId,
			String userId, String roleName, String finalJsonRequest, String uiJsonRequest, int paymentId,ErpSinglePaymentTransactionVo paymentTransactionVo)
					throws ApplicationException, IOException, JsonParseException, JsonMappingException {
		ObjectMapper objectMapper=new ObjectMapper();
		YesBankAuthorizationVo authorization = yesBankIntegrationDao.getCustomerAuthorization(organizationId,
				paymentTransferPaymentsVo.getData().getConsentId(),
				paymentTransferPaymentsVo.getData().getInitiation().getDebtorAccount().getIdentification(), userId,
				roleName);

		if (Objects.isNull(authorization)) {
			throw new ApplicationException("Customer Information Is Not Valid");
		}

		PaymentTransferResponseVo paymentTransferResponseVo = null;

		if (Objects.nonNull(finalJsonRequest)) {

			logger.info("Final Payment Transfer Request" + finalJsonRequest);
			String paymentTransferJsonRes = YesBank.getInstance().getSingleResponseEntity("domesticPayments",
					finalJsonRequest, authorization);
			logger.info("Final Payment Transfer Response" + paymentTransferJsonRes);

			JsonNode node = objectMapper.readTree(paymentTransferJsonRes);
			String code = null;
			if (Objects.nonNull(node.get("Code"))) {
				code = node.get("Code").asText();
				logger.info("Code:::" + code);
			}

			if (Objects.isNull(code)) {
				paymentTransferResponseVo = objectMapper.readValue(paymentTransferJsonRes,
						PaymentTransferResponseVo.class);
				logger.info("<<<<payment Transfer Response >>>>" + paymentTransferResponseVo);

				if (Objects.nonNull(paymentTransferResponseVo)) {

					yesBankIntegrationDao.updatePaymentTransferInfo(paymentTransferPaymentsVo, finalJsonRequest,
							paymentTransferResponseVo, paymentTransferJsonRes, organizationId, userId, roleName,
							uiJsonRequest, paymentId);
				}
			} else {
				throw new ApplicationException("Account is not available in YES Bank. Please register to proceed");
			}
			
			bulkPaymentsDao.insertIntoErpSinglePayment(paymentTransferPaymentsVo, paymentTransactionVo);
			
		}
		
		return paymentTransferResponseVo;
	}

=======
>>>>>>> 0a694d861a9a446c4b7c10a39cf5c7647a3014bd
}
