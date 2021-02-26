package com.blackstrawai.request.payroll.PayRun;



public class PayRunTableInfoRequest   {
	
	private Integer id;
	private String name;
	private String value;
	private String payType;
	private Boolean isShowColumn;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
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
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public Boolean getIsShowColumn() {
		return isShowColumn;
	}
	public void setIsShowColumn(Boolean isShowColumn) {
		this.isShowColumn = isShowColumn;
	}
	
	

}
