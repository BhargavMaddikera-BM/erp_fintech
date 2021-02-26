package com.blackstrawai.request.upload;

import com.blackstrawai.common.BaseRequest;

public class AccountingEntriesUploadRequest extends BaseRequest {

	private String date;
	private String type;
	private String journalNo;
	private String location;
	private String ledger;
	private String subLedger;
	private String currency;
	private String exchangeRate;
	private String credits;
	private String debits;

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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AccountingEntriesUploadRequest [date=");
		builder.append(date);
		builder.append(", type=");
		builder.append(type);
		builder.append(", journalNo=");
		builder.append(journalNo);
		builder.append(", location=");
		builder.append(location);
		builder.append(", ledger=");
		builder.append(ledger);
		builder.append(", subLedger=");
		builder.append(subLedger);
		builder.append(", currency=");
		builder.append(currency);
		builder.append(", exchangeRate=");
		builder.append(exchangeRate);
		builder.append(", credits=");
		builder.append(credits);
		builder.append(", debits=");
		builder.append(debits);
		builder.append("]");
		return builder.toString();
	}

}
