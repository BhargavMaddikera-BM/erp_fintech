package com.blackstrawai.externalintegration.banking.perfios;

public class PerfiosUserAccountTransactionVo {
	private String txnSeqId;
	private String xnDate;
	private String xnDetails;
	private String chequeNum;
	private String xnAmount;
	private String balance;
	private String categoryId;
	private String userComment;
	private String splitRefId;
	private String xnId;
	private String category;
	private String iType;
	private String accountName;
	private Long instId;
	private String instName;
	private String currency;
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getInstName() {
		return instName;
	}
	public void setInstName(String instName) {
		this.instName = instName;
	}
	private String status;
	private String accountId;
	
	public Long getInstId() {
		return instId;
	}
	public void setInstId(Long instId) {
		this.instId = instId;
	}
	public String getiType() {
		return iType;
	}
	public void setiType(String iType) {
		this.iType = iType;
	}
	
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getTxnSeqId() {
		return txnSeqId;
	}
	public void setTxnSeqId(String txnSeqId) {
		this.txnSeqId = txnSeqId;
	}
	public String getXnDate() {
		return xnDate;
	}
	public void setXnDate(String xnDate) {
		this.xnDate = xnDate;
	}
	public String getXnDetails() {
		return xnDetails;
	}
	public void setXnDetails(String xnDetails) {
		this.xnDetails = xnDetails;
	}
	public String getChequeNum() {
		return chequeNum;
	}
	public void setChequeNum(String chequeNum) {
		this.chequeNum = chequeNum;
	}
	public String getXnAmount() {
		return xnAmount;
	}
	public void setXnAmount(String xnAmount) {
		this.xnAmount = xnAmount;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getUserComment() {
		return userComment;
	}
	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}
	public String getSplitRefId() {
		return splitRefId;
	}
	public void setSplitRefId(String splitRefId) {
		this.splitRefId = splitRefId;
	}
	public String getXnId() {
		return xnId;
	}
	public void setXnId(String xnId) {
		this.xnId = xnId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
	
}
