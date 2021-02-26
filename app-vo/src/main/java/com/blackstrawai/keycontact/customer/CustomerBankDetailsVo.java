package com.blackstrawai.keycontact.customer;

public class CustomerBankDetailsVo {

	
	private Integer id;
	
	private String ifscCode;
	
	private String accountHolderName;
	
	private String accountNumber;
	
	private String confirmAccountNumber;
	
	private String upiId;
	
	private String bankName;
	
	private String branchName;
	
	private boolean isDefault;

	private String  status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
	public String getUpiId() {
		return upiId;
	}

	public void setUpiId(String upiId) {
		this.upiId = upiId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getConfirmAccountNumber() {
		return confirmAccountNumber;
	}

	public void setConfirmAccountNumber(String confirmAccountNumber) {
		this.confirmAccountNumber = confirmAccountNumber;
	}

	public String getAccountHolderName() {
		return accountHolderName;
	}

	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CustomerBankDetailsVo [id=");
		builder.append(id);
		builder.append(", ifscCode=");
		builder.append(ifscCode);
		builder.append(", accountHolderName=");
		builder.append(accountHolderName);
		builder.append(", accountNumber=");
		builder.append(accountNumber);
		builder.append(", confirmAccountNumber=");
		builder.append(confirmAccountNumber);
		builder.append(", upiId=");
		builder.append(upiId);
		builder.append(", bankName=");
		builder.append(bankName);
		builder.append(", branchName=");
		builder.append(branchName);
		builder.append(", isDefault=");
		builder.append(isDefault);
		builder.append(", status=");
		builder.append(status);
		builder.append("]");
		return builder.toString();
	}
	
	
	
}
