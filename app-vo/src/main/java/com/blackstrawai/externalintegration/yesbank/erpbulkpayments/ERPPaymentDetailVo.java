package com.blackstrawai.externalintegration.yesbank.erpbulkpayments;

import com.blackstrawai.ap.payment.noncore.PaymentNonCoreVo;

public class ERPPaymentDetailVo {

	private Integer id;
	
	private String uniqueIdentifier;
	
	private Integer keyContactId;
	
	private String keyContactType;
	
	private Integer keyContactAccountId;
	
	private String keyContactAccountNo;
	
	private String keyContactAccountName;
	
	private String keyContactIfsc;
	
	private Integer referenceModuleId;
	
	private String referenceModuleNo;
	
	private String paymentMode;
	
	private String amount;
	
	private String amountDescription;
	
	private String status;
	
	private PaymentNonCoreVo erpPaymentDetails;
	

	public PaymentNonCoreVo getErpPaymentDetails() {
		return erpPaymentDetails;
	}

	public void setErpPaymentDetails(PaymentNonCoreVo erpPaymentDetails) {
		this.erpPaymentDetails = erpPaymentDetails;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	public void setUniqueIdentifier(String uniqueIdentifier) {
		this.uniqueIdentifier = uniqueIdentifier;
	}

	public Integer getKeyContactId() {
		return keyContactId;
	}

	public void setKeyContactId(Integer keyContactId) {
		this.keyContactId = keyContactId;
	}

	public String getKeyContactType() {
		return keyContactType;
	}

	public void setKeyContactType(String keyContactType) {
		this.keyContactType = keyContactType;
	}

	public Integer getKeyContactAccountId() {
		return keyContactAccountId;
	}

	public void setKeyContactAccountId(Integer keyContactAccountId) {
		this.keyContactAccountId = keyContactAccountId;
	}

	public String getKeyContactAccountNo() {
		return keyContactAccountNo;
	}

	public void setKeyContactAccountNo(String keyContactAccountNo) {
		this.keyContactAccountNo = keyContactAccountNo;
	}

	public String getKeyContactAccountName() {
		return keyContactAccountName;
	}

	public void setKeyContactAccountName(String keyContactAccountName) {
		this.keyContactAccountName = keyContactAccountName;
	}

	public String getKeyContactIfsc() {
		return keyContactIfsc;
	}

	public void setKeyContactIfsc(String keyContactIfsc) {
		this.keyContactIfsc = keyContactIfsc;
	}

	public Integer getReferenceModuleId() {
		return referenceModuleId;
	}

	public void setReferenceModuleId(Integer referenceModuleId) {
		this.referenceModuleId = referenceModuleId;
	}

	public String getReferenceModuleNo() {
		return referenceModuleNo;
	}

	public void setReferenceModuleNo(String referenceModuleNo) {
		this.referenceModuleNo = referenceModuleNo;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getAmountDescription() {
		return amountDescription;
	}

	public void setAmountDescription(String amountDescription) {
		this.amountDescription = amountDescription;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

	
}
