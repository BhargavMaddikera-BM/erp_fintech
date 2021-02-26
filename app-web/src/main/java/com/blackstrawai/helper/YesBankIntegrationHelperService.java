package com.blackstrawai.helper;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.banking.dashboard.BankMasterAccountVo;
import com.blackstrawai.externalintegration.yesbank.YesBankCustomerInformationVo;
import com.blackstrawai.externalintegration.yesbank.YesBankNewAccountSetupVo;
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
import com.blackstrawai.externalintegration.yesbank.Request.fundconfirmation.AvailableBalanceDataVo;
import com.blackstrawai.externalintegration.yesbank.Request.fundconfirmation.AvailableBalanceDebtorAccountVo;
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
import com.blackstrawai.externalintegration.yesbank.Response.common.RequestData;
import com.blackstrawai.externalintegration.yesbank.Response.payments.CopyPaymentTransactionVo;
import com.blackstrawai.externalintegration.yesbank.statement.AdhocStatementReqVo;
import com.blackstrawai.externalintegration.yesbank.statement.ConsumerContextVo;
import com.blackstrawai.externalintegration.yesbank.statement.ReqBodyVo;
import com.blackstrawai.externalintegration.yesbank.statement.ReqHdrVo;
import com.blackstrawai.externalintegration.yesbank.statement.ServiceContextVo;
import com.blackstrawai.externalintegration.yesbank.statement.YesBankStatementVo;
import com.blackstrawai.request.externalintegration.banking.common.BankStatementRequest;
import com.blackstrawai.request.externalintegration.banking.yesbank.AvailableBalanceRequest;
import com.blackstrawai.request.externalintegration.banking.yesbank.BeneficiaryRequest;
import com.blackstrawai.request.externalintegration.banking.yesbank.BulkPaymentDetailsRequest;
import com.blackstrawai.request.externalintegration.banking.yesbank.BulkPaymentRequest;
import com.blackstrawai.request.externalintegration.banking.yesbank.PaymentDetailsRequest;
import com.blackstrawai.request.externalintegration.banking.yesbank.PaymentTransferRequest;
import com.blackstrawai.request.externalintegration.banking.yesbank.YesBankCustomerInformationRequest;
import com.blackstrawai.request.externalintegration.banking.yesbank.YesBankNewAccountSetupRequest;
import com.blackstrawai.yesbank.YesBankConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class YesBankIntegrationHelperService {

  private final Logger logger = Logger.getLogger(YesBankIntegrationHelperService.class);

  public YesBankCustomerInformationVo convertRequestToVo(
      YesBankCustomerInformationRequest requestObj) throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    String requestJson = objectMapper.writeValueAsString(requestObj);

    YesBankCustomerInformationVo yesBankCustomerInformationVo =
        objectMapper.readValue(requestJson, YesBankCustomerInformationVo.class);

    return yesBankCustomerInformationVo;
  }

  public CopyPaymentTransactionVo convertRequestToVo(
          PaymentTransferRequest requestObj) throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    String requestJson = objectMapper.writeValueAsString(requestObj);

    CopyPaymentTransactionVo paymentInformationVo =
            objectMapper.readValue(requestJson, CopyPaymentTransactionVo.class);

    return paymentInformationVo;
  }

  public YesBankNewAccountSetupVo convertRequestToNewAccountSetupVo(
      YesBankNewAccountSetupRequest requestObj) throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    String requestJson = objectMapper.writeValueAsString(requestObj);

    YesBankNewAccountSetupVo yesBankCustomerInformationVo =
        objectMapper.readValue(requestJson, YesBankNewAccountSetupVo.class);

    return yesBankCustomerInformationVo;
  }

  public YesBankStatementVo convertRequestToBankStatementVo(BankStatementRequest requestObj) {

    ReqHdrVo reqHdr = new ReqHdrVo();
    ConsumerContextVo consumerContextVo = new ConsumerContextVo();
    consumerContextVo.setRequesterID("APP");
    reqHdr.setConsumerContext(consumerContextVo);

    ServiceContextVo serviceContext = new ServiceContextVo();
    serviceContext.setReqRefNum(requestObj.getReqRefNo());
    serviceContext.setReqRefTimeStamp(requestObj.getReqRefTimeStamp());
    serviceContext.setServiceName(requestObj.getServiceName());
    serviceContext.setServiceVersionNo("1.0");
    reqHdr.setServiceContext(serviceContext);

    ReqBodyVo reqBody = new ReqBodyVo();
    reqBody.setCustomerId(requestObj.getCustomerId());
    reqBody.setAccountNo(requestObj.getAccountNo());
    reqBody.setTxnStartDate(requestObj.getTransactionStartDate());
    reqBody.setTxnEndDate(requestObj.getTransactionEndDate());

    AdhocStatementReqVo adhocStatementReq = new AdhocStatementReqVo();
    adhocStatementReq.setReqHdr(reqHdr);
    adhocStatementReq.setReqBody(reqBody);

    YesBankStatementVo yesBankStatement = new YesBankStatementVo();
    yesBankStatement.setAdhocStatementReq(adhocStatementReq);

    return yesBankStatement;
  }

  public PaymentTransferVo convertPaymentTransferRequestToVo(PaymentTransferRequest data) {

    PaymentTransferDomesticPaymentsVo domesticPaymentVo = new PaymentTransferDomesticPaymentsVo();
    domesticPaymentVo.setConsentId(data.getCustomerId());

    PaymentDetailsRequest paymentDetailsReq = data.getSinglePayment();
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

  
  public BulkPaymentVo convertBulkPaymentTransferRequestToVo(BulkPaymentRequest data ,YesBankCustomerInformationVo debtorDetails) {
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
		bulkPaymentVo.setFileIdentifier(uuid.toString());
		bulkPaymentVo.setNumberOfTransactions(String.valueOf(data.getPaymentDetails().size()));
		bulkPaymentVo.setConsentId(data.getCustomerId());
		bulkPaymentVo.setControSum(data.getControSum());
		bulkPaymentVo.setSecondaryIdentification(data.getCustomerId());
		List<BulkPaymentDomesticPaymentVo> domesticPaymentList = new ArrayList<BulkPaymentDomesticPaymentVo>();
		
		for(BulkPaymentDetailsRequest paymentDetails : data.getPaymentDetails()) {
		BulkPaymentDomesticPaymentVo domesticPayment = new BulkPaymentDomesticPaymentVo();
		domesticPayment.setConsentId(data.getCustomerId());
		
		BulkPaymentInitiationVo initiationVo = new BulkPaymentInitiationVo();
		initiationVo.setInstructionIdentification(generateBulkTransferSeqNo());
		initiationVo.setClearingSystemIdentification(paymentDetails.getTransactionType());
		
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
		creditorAccount.setSchemeName(paymentDetails.getIfsc());
		creditorAccount.setIdentification(paymentDetails.getBeneficiaryAccountNo());
		creditorAccount.setName(paymentDetails.getBeneficiaryName());
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
		remittanceUnstructure.setCreditorReferenceInformation(paymentDetails.getRemarks()!=null ? paymentDetails.getRemarks() : data.getPaymentDescription());
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
  
  private String generatePaymentTransferSeqNo(String paymentTransferType) {

    Random objGenerator = new Random();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyHHmm");
    LocalDateTime localDate = LocalDateTime.now();

    int sequenceNo = objGenerator.nextInt(100000);
    String paymentTransferSeqNo = dtf.format(localDate) + paymentTransferType + sequenceNo;
    logger.info("paymentTransferSeqNo>>>" + paymentTransferSeqNo);

    return paymentTransferSeqNo;
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
  
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
public RequestData convertAvailableBalanceRequestToVo(
      AvailableBalanceRequest availableBalanceRequest) {

    AvailableBalanceDebtorAccountVo availableBalanceDebtorAccountVo =
        new AvailableBalanceDebtorAccountVo();
    availableBalanceDebtorAccountVo.setConsentId(availableBalanceRequest.getCustomerId());
    availableBalanceDebtorAccountVo.setIdentification(availableBalanceRequest.getAccountNumber());
    availableBalanceDebtorAccountVo.setSecondaryIdentification(
        availableBalanceRequest.getCustomerId());

    AvailableBalanceDataVo availableBalanceDataVo = new AvailableBalanceDataVo();
    availableBalanceDataVo.setDebtorAccountVo(availableBalanceDebtorAccountVo);

    RequestData requestData = new RequestData<>();
    requestData.setData(availableBalanceDataVo);

    return requestData;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
public RequestData getAvailableBalanceRequestObject(String customerId, String accountNumber) throws ApplicationException {
	  RequestData requestData = new RequestData<>();
	  try {
	  AvailableBalanceDebtorAccountVo availableBalanceDebtorAccountVo =
        new AvailableBalanceDebtorAccountVo();
    availableBalanceDebtorAccountVo.setConsentId(customerId);
    availableBalanceDebtorAccountVo.setIdentification(accountNumber);
    availableBalanceDebtorAccountVo.setSecondaryIdentification(customerId);

    AvailableBalanceDataVo availableBalanceDataVo = new AvailableBalanceDataVo();
    availableBalanceDataVo.setDebtorAccountVo(availableBalanceDebtorAccountVo);
    
    requestData.setData(availableBalanceDataVo);
   }catch (Exception e) {
	   logger.error("Error in get availble balance:",e);
	   throw new ApplicationException(e);
}
    return requestData;
  }

  public BankMasterAccountVo convertBankMasterAccountsVoFromRequest(
      BeneficiaryRequest bankMasterAccountRequest) {
    BankMasterAccountVo bankMasterAccountVo = new BankMasterAccountVo();
    bankMasterAccountVo.setRoleName(bankMasterAccountRequest.getRoleName());
    bankMasterAccountVo.setAccountCode(bankMasterAccountRequest.getAccountCode());
    bankMasterAccountVo.setAccountCurrencyId(bankMasterAccountRequest.getAccountCurrencyId());
    bankMasterAccountVo.setAccountName(bankMasterAccountRequest.getAccountName());
    bankMasterAccountVo.setAccountNumber(bankMasterAccountRequest.getAccountNumber());
    bankMasterAccountVo.setAccountType(Integer.parseInt(bankMasterAccountRequest.getAccountType()));
    bankMasterAccountVo.setAccountVariant(bankMasterAccountRequest.getAccountVariant());
    bankMasterAccountVo.setBankName(bankMasterAccountRequest.getBankName());
    bankMasterAccountVo.setBranchName(bankMasterAccountRequest.getBranchName());
    bankMasterAccountVo.setCurrentBalance(bankMasterAccountRequest.getCurrentBalance());
    bankMasterAccountVo.setId(bankMasterAccountRequest.getId());
    bankMasterAccountVo.setIfscCode(bankMasterAccountRequest.getIfscCode());
    bankMasterAccountVo.setInterestRate(bankMasterAccountRequest.getInterestRate());
    bankMasterAccountVo.setIsSuperAdmin(bankMasterAccountRequest.getIsSuperAdmin());
    bankMasterAccountVo.setLimit(bankMasterAccountRequest.getLimit());
    bankMasterAccountVo.setMaturityDate(bankMasterAccountRequest.getMaturityDate());
    bankMasterAccountVo.setOpeningDate(bankMasterAccountRequest.getOpeningDate());
    bankMasterAccountVo.setOrganizationId(bankMasterAccountRequest.getOrganizationId());
    bankMasterAccountVo.setTermMonth(bankMasterAccountRequest.getTermMonth());
    bankMasterAccountVo.setTermYear(bankMasterAccountRequest.getTermYear());
    bankMasterAccountVo.setUserId(bankMasterAccountRequest.getUserId());
    return bankMasterAccountVo;
  }


}
