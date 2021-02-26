package com.blackstrawai.externalintegration.yesbank.Request.bulkpayments;

import java.sql.Date;
import java.sql.Timestamp;

public class BulkPaymentsListVo {
	
	private Integer id;
	
	private Date txnDate;
	
	private String txnNo;
	
	private Integer noOfTransaction;
	
	private Double totalAmount;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(Date txnDate) {
		this.txnDate = txnDate;
	}

	public String getTxnNo() {
		return txnNo;
	}

	public void setTxnNo(String txnNo) {
		this.txnNo = txnNo;
	}

	public Integer getNoOfTransaction() {
		return noOfTransaction;
	}

	public void setNoOfTransaction(Integer noOfTransaction) {
		this.noOfTransaction = noOfTransaction;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	
	
}
