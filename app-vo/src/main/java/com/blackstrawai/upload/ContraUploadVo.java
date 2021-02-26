package com.blackstrawai.upload;

public class ContraUploadVo {

	private String referenceNo;
	private String date;
	private String accountName;
	private String accountype;
	private String currency;
	private String exchangeRate;
	private String credit = "0";
	private String debit = "0";
	private String difference;
	private String status;
	private String totalCredits = "0";
	private String totalDebits = "0";
	private String totalCreditsEx = "0";
	private String totalDebitsEx = "0";

	public String getAccountype() {
		return accountype;
	}

	public void setAccountype(String accountype) {
		this.accountype = accountype;
	}

	public String getDifference() {
		return difference;
	}

	public void setDifference(String difference) {
		this.difference = difference;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTotalCredits() {
		return totalCredits;
	}

	public void setTotalCredits(String totalCredits) {
		this.totalCredits = totalCredits;
	}

	public String getTotalDebits() {
		return totalDebits;
	}

	public void setTotalDebits(String totalDebits) {
		this.totalDebits = totalDebits;
	}

	public String getTotalCreditsEx() {
		return totalCreditsEx;
	}

	public void setTotalCreditsEx(String totalCreditsEx) {
		this.totalCreditsEx = totalCreditsEx;
	}

	public String getTotalDebitsEx() {
		return totalDebitsEx;
	}

	public void setTotalDebitsEx(String totalDebitsEx) {
		this.totalDebitsEx = totalDebitsEx;
	}

	private String responseStatus;
	private String responseMessage;

	public String getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getDebit() {
		return debit;
	}

	public void setDebit(String debit) {
		this.debit = debit;
	}

}
