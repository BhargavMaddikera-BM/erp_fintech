package com.blackstrawai.upload;

public class AccountingEntriesUploadVo {

	private String date;
	private String type;
	private String journalNo;
	private String location;
	private String ledger;
	private String subLedger;
	private String currency;
	private String exchangeRate;
	private String credits="0";
	private String debits="0";
	private String responseStatus;
	private String responseMessage;
	private String difference;
	private String status;
	private String totalCredits="0";
	private String totalDebits="0";

	private String totalCreditsEx="0";
	private String totalDebitsEx="0";
	
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDifference() {
		return difference;
	}

	public void setDifference(String difference) {
		this.difference = difference;
	}

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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getJournalNo() {
		return journalNo;
	}

	public void setJournalNo(String journalNo) {
		this.journalNo = journalNo;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLedger() {
		return ledger;
	}

	public void setLedger(String ledger) {
		this.ledger = ledger;
	}

	public String getSubLedger() {
		return subLedger;
	}

	public void setSubLedger(String subLedger) {
		this.subLedger = subLedger;
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

	public String getCredits() {
		return credits;
	}

	public void setCredits(String credits) {
		this.credits = credits;
	}

	public String getDebits() {
		return debits;
	}

	public void setDebits(String debits) {
		this.debits = debits;
	}
}
