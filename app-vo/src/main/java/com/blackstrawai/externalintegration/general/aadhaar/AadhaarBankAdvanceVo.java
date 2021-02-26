package com.blackstrawai.externalintegration.general.aadhaar;

public class AadhaarBankAdvanceVo {

	private String beneficiaryName;
	private String bankRef;
	private String Remark;
	private String Status;
	private String amount;
	private String message;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getBeneficiaryName() {
		return beneficiaryName;
	}
	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}
	public String getBankRef() {
		return bankRef;
	}
	public void setBankRef(String bankRef) {
		this.bankRef = bankRef;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
		
}
