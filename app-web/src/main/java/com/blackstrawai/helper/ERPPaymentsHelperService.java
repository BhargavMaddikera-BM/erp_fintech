package com.blackstrawai.helper;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.externalintegration.banking.yesbank.ERPBulkPaymentsDao;
import com.blackstrawai.externalintegration.banking.yesbank.YesBankIntegrationDao;
import com.blackstrawai.externalintegration.yesbank.YesBankCustomerInformationVo;
import com.blackstrawai.externalintegration.yesbank.Request.bulkpayments.BulkPaymentContactInformationVo;
import com.blackstrawai.externalintegration.yesbank.Request.bulkpayments.BulkPaymentCreditorAccountVo;
import com.blackstrawai.externalintegration.yesbank.Request.bulkpayments.BulkPaymentDataVo;
import com.blackstrawai.externalintegration.yesbank.Request.bulkpayments.BulkPaymentDebtorAccountVo;
import com.blackstrawai.externalintegration.yesbank.Request.bulkpayments.BulkPaymentDeliveryAddressVo;
import com.blackstrawai.externalintegration.yesbank.Request.bulkpayments.BulkPaymentDomesticPaymentVo;
import com.blackstrawai.externalintegration.yesbank.Request.bulkpayments.BulkPaymentIdentitiesVo;
import com.blackstrawai.externalintegration.yesbank.Request.bulkpayments.BulkPaymentInitiationVo;
import com.blackstrawai.externalintegration.yesbank.Request.bulkpayments.BulkPaymentInstructedAmountVo;
import com.blackstrawai.externalintegration.yesbank.Request.bulkpayments.BulkPaymentRemittanceInformationVo;
import com.blackstrawai.externalintegration.yesbank.Request.bulkpayments.BulkPaymentRemittanceUnstructuredVo;
import com.blackstrawai.externalintegration.yesbank.Request.bulkpayments.BulkPaymentRiskVo;
import com.blackstrawai.externalintegration.yesbank.Request.bulkpayments.BulkPaymentUnstructuredVo;
import com.blackstrawai.externalintegration.yesbank.Request.bulkpayments.BulkPaymentVo;
import com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer.PaymentTransferContactInfoUnstructuredVo;
import com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer.PaymentTransferContactInfoVo;
import com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer.PaymentTransferCreditorAccountVo;
import com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer.PaymentTransferDebtorAccountVo;
import com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer.PaymentTransferDeliveryAddressVo;
import com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer.PaymentTransferDomesticPaymentsVo;
import com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer.PaymentTransferInitiationVo;
import com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer.PaymentTransferInstructedAmountVo;
import com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer.PaymentTransferRemittanceInformationVo;
import com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer.PaymentTransferRemittanceUnstructuredVo;
import com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer.PaymentTransferRiskVo;
import com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer.PaymentTransferVo;
import com.blackstrawai.externalintegration.yesbank.Response.YesBankAuthorizationVo;
import com.blackstrawai.externalintegration.yesbank.Response.payments.ErpSinglePaymentTransactionVo;
import com.blackstrawai.externalintegration.yesbank.Response.paymenttransfer.PaymentTransferResponseVo;
import com.blackstrawai.externalintegration.yesbank.erpbulkpayments.BaseERPPaymentVo;
import com.blackstrawai.externalintegration.yesbank.erpbulkpayments.ERPPaymentDetailVo;
import com.blackstrawai.request.externalintegration.banking.yesbank.BaseERPPaymentRequest;
import com.blackstrawai.request.externalintegration.banking.yesbank.BulkPaymentDetailsRequest;
import com.blackstrawai.request.externalintegration.banking.yesbank.ErpSinglePaymentDetailsRequest;
import com.blackstrawai.request.externalintegration.banking.yesbank.ErpSinglePaymentRequest;
import com.blackstrawai.request.externalintegration.banking.yesbank.PaymentDetailsRequest;
import com.blackstrawai.request.externalintegration.banking.yesbank.PaymentTransferRequest;
import com.blackstrawai.settings.SettingsModuleOrganizationDao;
import com.blackstrawai.settings.SettingsModuleOrganizationVo;
import com.blackstrawai.workflow.WorkflowConstants;
import com.blackstrawai.workflow.WorkflowProcessHelperService;
import com.blackstrawai.workflow.WorkflowProcessService;
import com.blackstrawai.workflow.WorkflowSettingsVo;
import com.blackstrawai.workflow.WorkflowThread;
import com.blackstrawai.yesbank.YesBank;
import com.blackstrawai.yesbank.YesBankConstants;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ERPPaymentsHelperService {

	  private final Logger logger = Logger.getLogger(ERPPaymentsHelperService.class);
	  private final ObjectMapper objectMapper = new ObjectMapper();

	  
	  @Autowired
	  private YesBankIntegrationDao yesBankIntegrationDao;
	  
	  @Autowired
	  private ERPBulkPaymentsDao erpbulkpaymentsdao;

	  @Autowired
		private WorkflowProcessHelperService workflowProcessHelperService;
		
		@Autowired
		private WorkflowProcessService workflowProcessService;
		
		@Autowired
		private SettingsModuleOrganizationDao settingsModuleOrganizationDao;

	  public BaseERPPaymentVo convertRequestToVo(
			  BaseERPPaymentRequest requestObj) throws IOException {

		    ObjectMapper objectMapper = new ObjectMapper();
		    String requestJson = objectMapper.writeValueAsString(requestObj);

		    BaseERPPaymentVo erpPaymentInfo =
		        objectMapper.readValue(requestJson, BaseERPPaymentVo.class);

		    return erpPaymentInfo;
		  }

	public BulkPaymentVo convertERPBulkPaymentRequestToYesBankRequestFormat(BaseERPPaymentVo data,
			YesBankCustomerInformationVo debtorDetails) {
		logger.info("Request ::"+ data);
		FileReader reader;
		BulkPaymentVo bulkpayment = new BulkPaymentVo();
		try {
			reader = new FileReader(YesBankConstants.DECIFER_CONFIG_PROPERTTY_URL);
		
		Properties p = new Properties();
		p.load(reader);

		String debitorIfsc  = p.getProperty(YesBankConstants.YES_BANK_BULK_SCHEMA);
		BulkPaymentDataVo bulkPaymentVo = new BulkPaymentDataVo();
		String uuidString = data.getCustomerId()+System.currentTimeMillis()+data.getOrgId();
		UUID uuid = UUID.nameUUIDFromBytes(uuidString.getBytes());
		data.setFileIdentifier(uuid.toString());
		bulkPaymentVo.setFileIdentifier(uuid.toString());
		bulkPaymentVo.setNumberOfTransactions(String.valueOf(data.getPaymentDetails().size()));
		bulkPaymentVo.setConsentId(data.getCustomerId());
		bulkPaymentVo.setControSum(data.getTotalAmount());
		bulkPaymentVo.setSecondaryIdentification(data.getCustomerId());
		List<BulkPaymentDomesticPaymentVo> domesticPaymentList = new ArrayList<BulkPaymentDomesticPaymentVo>();
		
		for(ERPPaymentDetailVo paymentDetails : data.getPaymentDetails()) {
		BulkPaymentDomesticPaymentVo domesticPayment = new BulkPaymentDomesticPaymentVo();
		domesticPayment.setConsentId(data.getCustomerId());
		
		BulkPaymentInitiationVo initiationVo = new BulkPaymentInitiationVo();
		String uniqueIdentifier = generateBulkTransferSeqNo();
		initiationVo.setInstructionIdentification(uniqueIdentifier);
		paymentDetails.setUniqueIdentifier(uniqueIdentifier);
		initiationVo.setClearingSystemIdentification(paymentDetails.getPaymentMode());
		
		BulkPaymentInstructedAmountVo instructedAmnt = new BulkPaymentInstructedAmountVo();
		instructedAmnt.setAmount(paymentDetails.getAmount());
		instructedAmnt.setCurrency("INR");
		initiationVo.setInstructedAmount(instructedAmnt);
		
		BulkPaymentDebtorAccountVo debtorAccount = new  BulkPaymentDebtorAccountVo();
		debtorAccount.setSchemeName(debitorIfsc);
		debtorAccount.setIdentification(debtorDetails.getAccountNo());
		debtorAccount.setName(debtorDetails.getAccountName()!=null ? debtorDetails.getAccountName() : "");
		debtorAccount.setSecondaryIdentification(data.getCustomerId());
		BulkPaymentUnstructuredVo debtorUnstructured = new BulkPaymentUnstructuredVo();
		BulkPaymentContactInformationVo contact = new BulkPaymentContactInformationVo();
		contact.setMobileNumber(debtorDetails.getMobileNo()!=null ?debtorDetails.getMobileNo().replace("+91", "") : null);
		debtorUnstructured.setContactInformation(contact);
		BulkPaymentIdentitiesVo identity = new BulkPaymentIdentitiesVo();
		identity.setMobileNumber(debtorDetails.getMobileNo().replace("+91", ""));
		debtorUnstructured.setIdentities(identity);
		debtorAccount.setUnstructured(debtorUnstructured);
		initiationVo.setDebtorAccount(debtorAccount);
		
		
		BulkPaymentCreditorAccountVo creditorAccount = new BulkPaymentCreditorAccountVo();
		creditorAccount.setSchemeName(paymentDetails.getKeyContactIfsc());
		creditorAccount.setIdentification(paymentDetails.getKeyContactAccountNo());
		creditorAccount.setName(paymentDetails.getKeyContactAccountName());
		BulkPaymentUnstructuredVo unstructured = new BulkPaymentUnstructuredVo();
		BulkPaymentContactInformationVo conatct = new BulkPaymentContactInformationVo();
		conatct.setMobileNumber("12345678");
		unstructured.setContactInformation(conatct);
		BulkPaymentIdentitiesVo ident = new BulkPaymentIdentitiesVo();
		ident.setMobileNumber("12345678");
		unstructured.setIdentities(ident);
		creditorAccount.setUnstructured(unstructured);
		initiationVo.setCreditorAccount(creditorAccount);
		
		
		BulkPaymentRemittanceInformationVo remittance = new  BulkPaymentRemittanceInformationVo();
		BulkPaymentRemittanceUnstructuredVo remittanceUnstructure = new BulkPaymentRemittanceUnstructuredVo();
		remittanceUnstructure.setRemitterAccount(debtorDetails.getAccountNo());
		remittanceUnstructure.setCreditorReferenceInformation(paymentDetails.getAmountDescription()!=null ? paymentDetails.getAmountDescription() : data.getPaymentDescription());
		remittance.setUnstructured(remittanceUnstructure);
		
		initiationVo.setRemittanceInformation(remittance);
		
		BulkPaymentRiskVo risk = new BulkPaymentRiskVo();
		risk.setPaymentContextCode("BankTransfer");
		BulkPaymentDeliveryAddressVo deliveryAddress = new BulkPaymentDeliveryAddressVo();
		deliveryAddress.setCountry("IN");
		List<String> addressline = new ArrayList<String>();
		addressline.add("PHALTAN");
		deliveryAddress.setAddressLine(addressline);
		risk.setDeliveryAddress(deliveryAddress);
		domesticPayment.setInitiation(initiationVo);
		domesticPayment.setRisk(risk);
		
		domesticPaymentList.add(domesticPayment);
		}
		bulkPaymentVo.setDomesticPayments(domesticPaymentList);
		logger.info("domesticPaymentList::"+domesticPaymentList);
		bulkpayment.setData(bulkPaymentVo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bulkpayment;
	}
	  

	  private String generateBulkTransferSeqNo() {
		  Random objGenerator = new Random();
	  String sequence = BigInteger.valueOf(Math.abs(objGenerator.nextLong())).toString(32).toUpperCase();
	  if (sequence.length() > 8) {
	      if (sequence.length() > 10) {
	      	sequence = sequence.substring(sequence.length() - 10);
	      }
	  }
	  long sequenceNo = objGenerator.nextInt(10000);
	  String paymentTransferSeqNo = sequence + "BT" + sequenceNo;
	  logger.info("paymentTransferSeqNo>>>" + paymentTransferSeqNo);

	  return paymentTransferSeqNo;
	  }
	  
	  public ErpSinglePaymentTransactionVo convertSingleRequestToVo(
			  ErpSinglePaymentRequest requestObj) throws IOException {

	    ObjectMapper objectMapper = new ObjectMapper();
	    String requestJson = objectMapper.writeValueAsString(requestObj);

	    ErpSinglePaymentTransactionVo singlepaymentInformationVo =
	            objectMapper.readValue(requestJson, ErpSinglePaymentTransactionVo.class);

	    return singlepaymentInformationVo;
	  }

	  public PaymentTransferVo convertPaymentTransferRequestToVo(ErpSinglePaymentRequest data) {

		    PaymentTransferDomesticPaymentsVo domesticPaymentVo = new PaymentTransferDomesticPaymentsVo();
		    domesticPaymentVo.setConsentId(data.getCustomerId());
		    
		    ErpSinglePaymentDetailsRequest paymentDetailsReq = data.getSinglePayment();
		    PaymentTransferInitiationVo initiationVo = new PaymentTransferInitiationVo();
		    initiationVo.setInstructionIdentification(
		        generatePaymentTransferSeqNo(data.getSinglePayment().getPaymentMode()));
		    initiationVo.setEndToEndIdentification("");

		    PaymentTransferInstructedAmountVo instructedAmountVo = new PaymentTransferInstructedAmountVo();
		    instructedAmountVo.setAmount(paymentDetailsReq.getAmount());
		    instructedAmountVo.setCurrency("INR");
		    initiationVo.setInstructedAmount(instructedAmountVo);

		    PaymentTransferDebtorAccountVo debtorAccountVo = new PaymentTransferDebtorAccountVo();
		    debtorAccountVo.setIdentification(data.getAccountNumber());
		    debtorAccountVo.setSecondaryIdentification(data.getCustomerId());
		    initiationVo.setDebtorAccount(debtorAccountVo);

		    PaymentTransferCreditorAccountVo creditorAccountVo = new PaymentTransferCreditorAccountVo();
		    creditorAccountVo.setSchemeName(paymentDetailsReq.getIfscCode());
		    creditorAccountVo.setIdentification(paymentDetailsReq.getBeneficiaryAccountNumber());
		    creditorAccountVo.setName(paymentDetailsReq.getBeneficiaryName());
		    creditorAccountVo.setBeneficiaryId(paymentDetailsReq.getBeneficiaryId());
		    creditorAccountVo.setBeneficiaryType(paymentDetailsReq.getBeneficiaryType());
		    initiationVo.setCreditorAccount(creditorAccountVo);

		    PaymentTransferContactInfoUnstructuredVo unstructuredVo =
		            new PaymentTransferContactInfoUnstructuredVo();

		    PaymentTransferContactInfoVo contactInformationVo = new PaymentTransferContactInfoVo();
		    if(!(paymentDetailsReq.getEmailId().isEmpty() || paymentDetailsReq.getMobileNo().isEmpty())){
		      contactInformationVo.setEmailAddress(paymentDetailsReq.getEmailId());
		      contactInformationVo.setMobileNumber(paymentDetailsReq.getMobileNo());
		      unstructuredVo.setContactInformation(contactInformationVo);
		      creditorAccountVo.setUnstructured(unstructuredVo);
		    }

		    PaymentTransferRemittanceInformationVo remittanceInformationVo =
		        new PaymentTransferRemittanceInformationVo();
		    remittanceInformationVo.setReference(paymentDetailsReq.getDescription());
		    remittanceInformationVo.setBillReference(paymentDetailsReq.getBillRef());
		    remittanceInformationVo.setPaymentReference(paymentDetailsReq.getPaymentRef());

		    PaymentTransferRemittanceUnstructuredVo remittanceUnstructuredVo =
		        new PaymentTransferRemittanceUnstructuredVo();
		    remittanceUnstructuredVo.setCreditorReferenceInformation(
		        paymentDetailsReq.getDescription().equals("")
		            ? "New Payment"
		            : paymentDetailsReq.getDescription());
		    remittanceInformationVo.setUnstructured(remittanceUnstructuredVo);
		    initiationVo.setRemittanceInformation(remittanceInformationVo);

		    initiationVo.setClearingSystemIdentification(paymentDetailsReq.getPaymentMode());
		    domesticPaymentVo.setInitiation(initiationVo);

		    PaymentTransferDeliveryAddressVo deliveryAddressVo = new PaymentTransferDeliveryAddressVo();
		    String str = "Flat 7, Acacia Lodge";
		    deliveryAddressVo.setAddressLine(Arrays.asList(str));
		    deliveryAddressVo.setStreetName("Acacia Avenue");
		    deliveryAddressVo.setBuildingNumber("27");
		    deliveryAddressVo.setPostCode("600524");
		    deliveryAddressVo.setTownName("MUM");
		    String CountySubDivision = "MH";
		    deliveryAddressVo.setCountySubDivision(Arrays.asList(CountySubDivision));
		    deliveryAddressVo.setCountry("IN");

		    PaymentTransferRiskVo riskVo = new PaymentTransferRiskVo();
		    riskVo.setDeliveryAddress(deliveryAddressVo);

		    PaymentTransferVo paymentTransferVo = new PaymentTransferVo();
		    paymentTransferVo.setData(domesticPaymentVo);
		    paymentTransferVo.setRisk(riskVo);

		    return paymentTransferVo;
		  }

	  private String generatePaymentTransferSeqNo(String paymentTransferType) {

		    Random objGenerator = new Random();
		    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyHHmm");
		    LocalDateTime localDate = LocalDateTime.now();

		    int sequenceNo = objGenerator.nextInt(100000);
		    String paymentTransferSeqNo = dtf.format(localDate) + paymentTransferType + sequenceNo;
		    logger.info("paymentTransferSeqNo>>>" + paymentTransferSeqNo);

		    return paymentTransferSeqNo;
		  }
	  
}
