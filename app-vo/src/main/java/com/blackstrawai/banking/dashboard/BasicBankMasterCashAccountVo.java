package com.blackstrawai.banking.dashboard;

public class BasicBankMasterCashAccountVo {

	//Column : id 
	private Integer value;
	
	//Column :cash_account_name
	private String name;
	
	
	private String accountCode;

	
	
	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
	
	
	
}
