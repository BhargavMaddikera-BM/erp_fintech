package com.blackstrawai.ap.payment.noncore;

public class PayrollDueItemVo {

	private int id;
	private String payPeriod;
	private String payRunReference;
	private String payRunAmount;
	private String paidAmount;
	private String status;
	private String payrunDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPayPeriod() {
		return payPeriod;
	}

	public void setPayPeriod(String payPeriod) {
		this.payPeriod = payPeriod;
	}

	public String getPayRunReference() {
		return payRunReference;
	}

	public void setPayRunReference(String payRunReference) {
		this.payRunReference = payRunReference;
	}

	public String getPayRunAmount() {
		return payRunAmount;
	}

	public void setPayRunAmount(String payRunAmount) {
		this.payRunAmount = payRunAmount;
	}

	public String getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(String paidAmount) {
		this.paidAmount = paidAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPayrunDate() {
		return payrunDate;
	}

	public void setPayrunDate(String payrunDate) {
		this.payrunDate = payrunDate;
	}

}
