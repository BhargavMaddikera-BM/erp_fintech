package com.blackstrawai.request.ar.applycredits;

public class ApplyCreditsInvoiceRequest {

	private Integer invoiceId;
	private String invoiceAmount;
	private String appliedAmount;
	private Integer id;
	private String status;
	private String bankCharges;
	private String tdsDeducted;
	private String others1;
	private String others2;
	private String others3;

	public String getBankCharges() {
		return bankCharges;
	}

	public void setBankCharges(String bankCharges) {
		this.bankCharges = bankCharges;
	}

	public String getTdsDeducted() {
		return tdsDeducted;
	}

	public void setTdsDeducted(String tdsDeducted) {
		this.tdsDeducted = tdsDeducted;
	}

	public String getOthers1() {
		return others1;
	}

	public void setOthers1(String others1) {
		this.others1 = others1;
	}

	public String getOthers2() {
		return others2;
	}

	public void setOthers2(String others2) {
		this.others2 = others2;
	}

	public String getOthers3() {
		return others3;
	}

	public void setOthers3(String others3) {
		this.others3 = others3;
	}

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

	public Integer getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(String invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public String getAppliedAmount() {
		return appliedAmount;
	}

	public void setAppliedAmount(String appliedAmount) {
		this.appliedAmount = appliedAmount;
	}

}
