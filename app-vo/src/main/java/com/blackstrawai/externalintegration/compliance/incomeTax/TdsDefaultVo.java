package com.blackstrawai.externalintegration.compliance.incomeTax;

/**
 * POJO class for TDS Default entries.
 * 
 * @author Aditya Bharadwaj
 *
 */
public class TdsDefaultVo {
	private String financialYear;
	private String shortPayment;
	private String shortDeduction;
	private String interestOnPayments;
	private String interestOnDeduction;
	private String lateFilingFee;
	private String interest220;
	private String totalDefault;
	public String getFinancialYear() {
		return financialYear;
	}
	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}
	public String getShortPayment() {
		return shortPayment;
	}
	public void setShortPayment(String shortPayment) {
		this.shortPayment = shortPayment;
	}
	public String getShortDeduction() {
		return shortDeduction;
	}
	public void setShortDeduction(String shortDeduction) {
		this.shortDeduction = shortDeduction;
	}
	public String getInterestOnPayments() {
		return interestOnPayments;
	}
	public void setInterestOnPayments(String interestOnPayments) {
		this.interestOnPayments = interestOnPayments;
	}
	public String getInterestOnDeduction() {
		return interestOnDeduction;
	}
	public void setInterestOnDeduction(String interestOnDeduction) {
		this.interestOnDeduction = interestOnDeduction;
	}
	public String getLateFilingFee() {
		return lateFilingFee;
	}
	public void setLateFilingFee(String lateFilingFee) {
		this.lateFilingFee = lateFilingFee;
	}
	public String getInterest220() {
		return interest220;
	}
	public void setInterest220(String interest220) {
		this.interest220 = interest220;
	}
	public String getTotalDefault() {
		return totalDefault;
	}
	public void setTotalDefault(String totalDefault) {
		this.totalDefault = totalDefault;
	}
	
	
}
