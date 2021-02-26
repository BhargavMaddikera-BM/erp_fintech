package com.blackstrawai.request.accounting;

import com.blackstrawai.common.BaseRequest;

public class AccountingAspectsItemsRequest extends BaseRequest {

	private Integer accountsId;
	private String accountsName;
	private String accountsLevel;
	private String subLedgerName;
	private Integer subLedgerId;
	private String description;
	private Integer currencyId;
	private String exchangeRate;
	private String credits;
	private String debits;
	private Integer id;
	private String status;
	private String totalCreditsEx;
	private String totalDebitsEx;
	
	

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

	public String getAccountsName() {
		return accountsName;
	}

	public void setAccountsName(String accountsName) {
		this.accountsName = accountsName;
	}

	public String getAccountsLevel() {
		return accountsLevel;
	}

	public void setAccountsLevel(String accountsLevel) {
		this.accountsLevel = accountsLevel;
	}

	public String getSubLedgerName() {
		return subLedgerName;
	}

	public void setSubLedgerName(String subLedgerName) {
		this.subLedgerName = subLedgerName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getAccountsId() {
		return accountsId;
	}

	public void setAccountsId(Integer accountsId) {
		this.accountsId = accountsId;
	}

	public Integer getSubLedgerId() {
		return subLedgerId;
	}

	public void setSubLedgerId(Integer subLedgerId) {
		this.subLedgerId = subLedgerId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
