package com.blackstrawai.ar.dropdowns;

import java.util.List;

import com.blackstrawai.banking.dashboard.BankMasterAccountBaseVo;

public class PaymentModeVo{
	private int id;
	private String name;
	private List<BankMasterAccountBaseVo> accountsList;
	public List<BankMasterAccountBaseVo> getAccountsList() {
		return accountsList;
	}
	public void setAccountsList(List<BankMasterAccountBaseVo> accountsList) {
		this.accountsList = accountsList;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
} 