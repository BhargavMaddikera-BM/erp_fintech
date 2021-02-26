package com.blackstrawai.request.banking.contra;

public class ContraEntriesRequest {

	private Integer accountId;
	private Integer currencyId;
	private String exchangeRate;
	private String debit;
	private String credit;
	private Integer id;
	private String status;
	private String totalCreditsEx;
	private String totalDebitsEx;
	private String accountType;
	private String transRefNo;

	public String getTransRefNo() {
		return transRefNo;
	}

	public void setTransRefNo(String transRefNo) {
		this.transRefNo = transRefNo;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public String getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getDebit() {
		return debit;
	}

	public void setDebit(String debit) {
		this.debit = debit;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
