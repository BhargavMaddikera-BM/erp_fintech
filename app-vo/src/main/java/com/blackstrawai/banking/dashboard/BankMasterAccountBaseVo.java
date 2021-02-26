package com.blackstrawai.banking.dashboard;

import com.blackstrawai.common.BaseVo;

public class BankMasterAccountBaseVo extends BaseVo {

	private String accountName;
	private String accountType;
	private String financialInstitution;
	private String deciferBalance;
	private String actualBalance;
	private Integer id;
	private String name;
	private String value;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getActualBalance() {
		return actualBalance;
	}

	public void setActualBalance(String actualBalance) {
		this.actualBalance = actualBalance;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getFinancialInstitution() {
		return financialInstitution;
	}

	public void setFinancialInstitution(String financialInstitution) {
		this.financialInstitution = financialInstitution;
	}

	public String getDeciferBalance() {
		return deciferBalance;
	}

	public void setDeciferBalance(String deciferBalance) {
		this.deciferBalance = deciferBalance;
	}

}
