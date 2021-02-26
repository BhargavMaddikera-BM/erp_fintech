package com.blackstrawai.ap.payment.noncore;

public class PaymentNonCoreRefundInvoiceVo {
	private Integer id;
	private String status;
	private String invoiceRef;
	private String invoiceAmount;
	private String refundAmount;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInvoiceRef() {
		return invoiceRef;
	}
	public void setInvoiceRef(String invoiceRef) {
		this.invoiceRef = invoiceRef;
	}
	public String getInvoiceAmount() {
		return invoiceAmount;
	}
	public void setInvoiceAmount(String invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	public String getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}
	
}
