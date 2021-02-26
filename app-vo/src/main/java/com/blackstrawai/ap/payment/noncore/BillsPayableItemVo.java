package com.blackstrawai.ap.payment.noncore;

public class BillsPayableItemVo {
	private int id;
	private String billNo;
	private String vendor;
	private String dueDate;
	private String amount;
	private String balanceDue;
	private String invoiceDate;
	private String status;
	private String currencySymbol;
	private Integer vendorId;
	private Integer currencyId;
	private Boolean isPaymentInitiated;
	
	
	public Boolean getIsPaymentInitiated() {
		return isPaymentInitiated;
	}

	public void setIsPaymentInitiated(Boolean isPaymentInitiated) {
		this.isPaymentInitiated = isPaymentInitiated;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public String getBalanceDue() {
		return balanceDue;
	}

	public void setBalanceDue(String balanceDue) {
		this.balanceDue = balanceDue;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
