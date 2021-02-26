package com.blackstrawai.banking.dashboard;

public class BasicBankMasterWalletVo {
	
	//Maps to the Id colum in DB 
	private Integer value;
	
	//Maps to the account name column in DB 
	private String name;
	
	private String authorisedPerson ;
	
	private String walletNumber;
	
	private String walletProvider;
	
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

	public String getAuthorisedPerson() {
		return authorisedPerson;
	}

	public void setAuthorisedPerson(String authorisedPerson) {
		this.authorisedPerson = authorisedPerson;
	}

	public String getWalletNumber() {
		return walletNumber;
	}

	public void setWalletNumber(String walletNumber) {
		this.walletNumber = walletNumber;
	}

	public String getWalletProvider() {
		return walletProvider;
	}

	public void setWalletProvider(String walletProvider) {
		this.walletProvider = walletProvider;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
	
	
	
	
}
