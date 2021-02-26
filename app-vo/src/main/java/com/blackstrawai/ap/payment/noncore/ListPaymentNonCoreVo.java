package com.blackstrawai.ap.payment.noncore;

import java.util.List;

public class ListPaymentNonCoreVo {
	private int id;
	private String paymentDate;
	private String paymentRefNo;
	private String paymentType;
	private String paidVia;
	private List<NameVo> contact;
	private String amount;
	private String status;
	private String contactType;
	private int paymentModeId;
	private String amountWithoutSymbol;
	private int currencyId;
	private int paidViaId;
	private int contactId;
	private List<NameVo> invoices;
	
	public String getAmountWithoutSymbol() {
		return amountWithoutSymbol;
	}
	public void setAmountWithoutSymbol(String amountWithoutSymbol) {
		this.amountWithoutSymbol = amountWithoutSymbol;
	}
	public int getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}
	public int getPaidViaId() {
		return paidViaId;
	}
	public void setPaidViaId(int paidViaId) {
		this.paidViaId = paidViaId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getPaymentRefNo() {
		return paymentRefNo;
	}
	public void setPaymentRefNo(String paymentRefNo) {
		this.paymentRefNo = paymentRefNo;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getPaidVia() {
		return paidVia;
	}
	public void setPaidVia(String paidVia) {
		this.paidVia = paidVia;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getContactType() {
		return contactType;
	}
	public void setContactType(String contactType) {
		this.contactType = contactType;
	}
	public int getPaymentModeId() {
		return paymentModeId;
	}
	public void setPaymentModeId(int paymentModeId) {
		this.paymentModeId = paymentModeId;
	}
	public int getContactId() {
		return contactId;
	}
	public void setContactId(int contactId) {
		this.contactId = contactId;
	}
	public List<NameVo> getInvoices() {
		return invoices;
	}
	public void setInvoices(List<NameVo> invoices) {
		this.invoices = invoices;
	}
	public List<NameVo> getContact() {
		return contact;
	}
	public void setContact(List<NameVo> contact) {
		this.contact = contact;
	}

	
}
