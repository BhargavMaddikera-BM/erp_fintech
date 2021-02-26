package com.blackstrawai.externalintegration.yesbank.beneficiary;

public class BeneficiaryListVo {

	private Integer contactId;
	
	private String beneficiaryType;
	
	private Integer bankId;
	
	private String accountName ;
	
	private String amountPaid;
	
	private String amountReceived;
	
	private String lastTransaction;
	
	private boolean isBeneficiaryScreen = true;
	
	private String accountNumber;
	
	private String ifscCode;
	
	// This attribute is needed for UI to use in Get single API
	private String type;
	
	private String status ;
	
	
	
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getContactId() {
		return contactId;
	}

	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}

	public String getBeneficiaryType() {
		return beneficiaryType;
	}

	public void setBeneficiaryType(String beneficiaryType) {
		this.beneficiaryType = beneficiaryType;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(String amountPaid) {
		this.amountPaid = amountPaid;
	}

	public String getAmountReceived() {
		return amountReceived;
	}

	public void setAmountReceived(String amountReceived) {
		this.amountReceived = amountReceived;
	}

	public String getLastTransaction() {
		return lastTransaction;
	}

	public void setLastTransaction(String lastTransaction) {
		this.lastTransaction = lastTransaction;
	}

	public boolean getIsBeneficiaryScreen() {
		return isBeneficiaryScreen;
	}

	public void setIsBeneficiaryScreen(boolean isBeneficiaryScreen) {
		this.isBeneficiaryScreen = isBeneficiaryScreen;
	}
	
	
	
}
