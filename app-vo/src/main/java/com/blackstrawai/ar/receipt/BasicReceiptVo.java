package com.blackstrawai.ar.receipt;

import com.blackstrawai.common.BaseVo;

public class BasicReceiptVo extends BaseVo{

	private Integer receiptId;
	
	private String receiptNo;
	
	private Double invoiceAmount;
	
	private Double receivedAmount;
	
	private Integer refundTypeId;
	
	private String receiptStatus ;
	private int id ;
	private String name ;
	private boolean isCurrencyEditable;
	


	public boolean isCurrencyEditable() {
		return isCurrencyEditable;
	}

	public void setCurrencyEditable(boolean isCurrencyEditable) {
		this.isCurrencyEditable = isCurrencyEditable;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public Integer getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(Integer receiptId) {
		this.receiptId = receiptId;
	}

	public Double getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(Double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public Double getReceivedAmount() {
		return receivedAmount;
	}

	public void setReceivedAmount(Double receivedAmount) {
		this.receivedAmount = receivedAmount;
	}

	public Integer getRefundTypeId() {
		return refundTypeId;
	}

	public void setRefundTypeId(Integer refundTypeId) {
		this.refundTypeId = refundTypeId;
	}

	public String getReceiptStatus() {
		return receiptStatus;
	}

	public void setReceiptStatus(String receiptStatus) {
		this.receiptStatus = receiptStatus;
	}
	
	
	
}
