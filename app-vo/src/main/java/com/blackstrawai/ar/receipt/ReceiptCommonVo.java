package com.blackstrawai.ar.receipt;

public class ReceiptCommonVo {
	private int id;
	private String name;
	private String type;
	private int value;
	private String dueAmount;
	private Integer currencyId;
	private String parentType;
	
	
	
	public String getParentType() {
		return parentType;
	}
	public void setParentType(String parentType) {
		this.parentType = parentType;
	}
	public Integer getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getDueAmount() {
		return dueAmount;
	}
	public void setDueAmount(String dueAmount) {
		this.dueAmount = dueAmount;
	}
	@Override
	public String toString() {
		return "ReceiptCommonVo [id=" + id + ", name=" + name + ", type=" + type + ", value=" + value + ", dueAmount="
				+ dueAmount + "]";
	}
	


}
