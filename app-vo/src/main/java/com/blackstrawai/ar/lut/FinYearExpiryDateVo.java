package com.blackstrawai.ar.lut;

public class FinYearExpiryDateVo {
	private String name;
	private String value;
	private String expiryDate;
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
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public FinYearExpiryDateVo(String name,String expiryDate) {
		super();
		this.name = name;
		this.value = name;
		this.expiryDate = expiryDate;
	}

}
