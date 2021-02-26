package com.blackstrawai.ap.dropdowns;

public class PaymentNonCoreColumnDropDownVo {

	private int id;
	private String name;
	private String value;
	
	public PaymentNonCoreColumnDropDownVo(int id, String name) {
		super();
		this.id = id;
		this.name = name;
		this.value = id+"";
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
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	

}
