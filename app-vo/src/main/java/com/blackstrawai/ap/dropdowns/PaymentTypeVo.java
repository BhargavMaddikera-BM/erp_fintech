package com.blackstrawai.ap.dropdowns;

public class PaymentTypeVo {
	private int id;
	private String name;
	private int value;
	private Integer currencyId;
	
	public PaymentTypeVo() {
		
	}
	
	public PaymentTypeVo(int id, String name, int value) {
		this.id = id;
		this.name = name;
		this.value = value;
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

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}
	
}
