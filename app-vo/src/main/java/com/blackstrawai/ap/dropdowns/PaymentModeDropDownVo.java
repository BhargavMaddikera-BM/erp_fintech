package com.blackstrawai.ap.dropdowns;

import java.util.List;

import com.blackstrawai.banking.dashboard.BankMasterAccountBaseVo;

public class PaymentModeDropDownVo {

	private int id;
	private String name;
	private List<BankMasterAccountBaseVo> child;
	private int value;
	
	public List<BankMasterAccountBaseVo> getChild() {
		return child;
	}
	public void setChild(List<BankMasterAccountBaseVo> child) {
		this.child = child;
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
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	

}
