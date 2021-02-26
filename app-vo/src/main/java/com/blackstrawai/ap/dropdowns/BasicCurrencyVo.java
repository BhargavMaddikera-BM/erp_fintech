package com.blackstrawai.ap.dropdowns;

public class BasicCurrencyVo {
	private int id;
	private String name;
	private String symbol;
	private String alternateSymbol;
	private Integer value;
	private String decimalValueDenoter;

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

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getAlternateSymbol() {
		return alternateSymbol;
	}

	public void setAlternateSymbol(String alternateSymbol) {
		this.alternateSymbol = alternateSymbol;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getDecimalValueDenoter() {
		return decimalValueDenoter;
	}

	public void setDecimalValueDenoter(String decimalValueDenoter) {
		this.decimalValueDenoter = decimalValueDenoter;
	}

}
