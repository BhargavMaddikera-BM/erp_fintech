package com.blackstrawai.externalintegration.yesbank.Response.bulkpayments;

public class BulkTransactionDetailVo {

	
	private String beneficiaryName;
	
	private String acountNo;
	
	private String ifsc;
	
	private String txnReference;
	
	private String txnType;
	
	private Double amount;
	
	private String narration;
	
	private String status;

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public String getAcountNo() {
		return acountNo;
	}

	public void setAcountNo(String acountNo) {
		this.acountNo = acountNo;
	}

	public String getIfsc() {
		return ifsc;
	}

	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}

	public String getTxnReference() {
		return txnReference;
	}

	public void setTxnReference(String txnReference) {
		this.txnReference = txnReference;
	}

	public String getTxnType() {
		return txnType;
	}

	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getNarration() {
		return narration;
	}

	public void setNarration(String narration) {
		this.narration = narration;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
