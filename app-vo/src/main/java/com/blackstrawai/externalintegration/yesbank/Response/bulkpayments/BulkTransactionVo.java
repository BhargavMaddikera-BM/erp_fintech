package com.blackstrawai.externalintegration.yesbank.Response.bulkpayments;

import java.util.List;

public class BulkTransactionVo {

	private String txnIdentifier;
	
	private String debitAccount;
	
	private String customerId;

	private String paymentDescription;
	
	private Double totalAmount;
	
	private List<BulkTransactionDetailVo> transactionDetails;
	
	private Integer noOfTxn;
	
	private Double totalSuccessAmount;
	
	private Double totalFailedAmount;
	
	

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Double getTotalSuccessAmount() {
		return totalSuccessAmount;
	}

	public void setTotalSuccessAmount(Double totalSuccessAmount) {
		this.totalSuccessAmount = totalSuccessAmount;
	}

	public Double getTotalFailedAmount() {
		return totalFailedAmount;
	}

	public void setTotalFailedAmount(Double totalFailedAmount) {
		this.totalFailedAmount = totalFailedAmount;
	}

	public Integer getNoOfTxn() {
		return noOfTxn;
	}

	public void setNoOfTxn(Integer noOfTxn) {
		this.noOfTxn = noOfTxn;
	}

	public String getTxnIdentifier() {
		return txnIdentifier;
	}

	public void setTxnIdentifier(String txnIdentifier) {
		this.txnIdentifier = txnIdentifier;
	}

	public String getDebitAccount() {
		return debitAccount;
	}

	public void setDebitAccount(String debitAccount) {
		this.debitAccount = debitAccount;
	}

	public String getPaymentDescription() {
		return paymentDescription;
	}

	public void setPaymentDescription(String paymentDescription) {
		this.paymentDescription = paymentDescription;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<BulkTransactionDetailVo> getTransactionDetails() {
		return transactionDetails;
	}

	public void setTransactionDetails(List<BulkTransactionDetailVo> transactionDetails) {
		this.transactionDetails = transactionDetails;
	}
	
	
}
