package com.blackstrawai.export;

public class AccountingEntryItemExportVo {

	private String ledger;
	private String subLedger;
	private String currency;
	private String exchangeRate;
	private String credits;
	private String debits;

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
