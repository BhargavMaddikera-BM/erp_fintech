package com.blackstrawai.ap.payment.noncore;

import java.util.List;

public class PaymentAdviceVo {
	private String orgName;
	private ContactAddressVo orgAddress;
	private String paymentNumber;
	private String paymentDate;
	private String bankReferenceNumber;
	private String placeOfSupplyName;
	private String placeOfSupplyCode;
	private String paymentMode;
	private String paidThrough;
	private String amountPaid;
	private String amountPaidInWords;
	private List<ContactAddressVo> paidTo;
	
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public ContactAddressVo getOrgAddress() {
		return orgAddress;
	}
	public void setOrgAddress(ContactAddressVo orgAddress) {
		this.orgAddress = orgAddress;
	}
	public String getPaymentNumber() {
		return paymentNumber;
	}
	public void setPaymentNumber(String paymentNumber) {
		this.paymentNumber = paymentNumber;
	}
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getBankReferenceNumber() {
		return bankReferenceNumber;
	}
	public void setBankReferenceNumber(String bankReferenceNumber) {
		this.bankReferenceNumber = bankReferenceNumber;
	}

	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getPaidThrough() {
		return paidThrough;
	}
	public void setPaidThrough(String paidThrough) {
		this.paidThrough = paidThrough;
	}
	public String getAmountPaid() {
		return amountPaid;
	}
	public void setAmountPaid(String amountPaid) {
		this.amountPaid = amountPaid;
	}
	public String getAmountPaidInWords() {
		return amountPaidInWords;
	}
	public void setAmountPaidInWords(String amountPaidInWords) {
		this.amountPaidInWords = amountPaidInWords;
	}
	public List<ContactAddressVo> getPaidTo() {
		return paidTo;
	}
	public void setPaidTo(List<ContactAddressVo> paidTo) {
		this.paidTo = paidTo;
	}
	public String getPlaceOfSupplyName() {
		return placeOfSupplyName;
	}
	public void setPlaceOfSupplyName(String placeOfSupplyName) {
		this.placeOfSupplyName = placeOfSupplyName;
	}
	public String getPlaceOfSupplyCode() {
		return placeOfSupplyCode;
	}
	public void setPlaceOfSupplyCode(String placeOfSupplyCode) {
		this.placeOfSupplyCode = placeOfSupplyCode;
	}
	
	
}
