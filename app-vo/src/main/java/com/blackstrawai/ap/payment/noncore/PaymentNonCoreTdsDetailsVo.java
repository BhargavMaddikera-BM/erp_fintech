package com.blackstrawai.ap.payment.noncore;

public class PaymentNonCoreTdsDetailsVo {
	private int id;
	private String status;
	private String tdsSection;
	private String type;
	private String paidAmt;
	private String tax;
	private String interest;
	private String penalty;
	private String others1;
	private String others2;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTdsSection() {
		return tdsSection;
	}
	public void setTdsSection(String tdsSection) {
		this.tdsSection = tdsSection;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPaidAmt() {
		return paidAmt;
	}
	public void setPaidAmt(String paidAmt) {
		this.paidAmt = paidAmt;
	}
	public String getTax() {
		return tax;
	}
	public void setTax(String tax) {
		this.tax = tax;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getPenalty() {
		return penalty;
	}
	public void setPenalty(String penalty) {
		this.penalty = penalty;
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
	
}
