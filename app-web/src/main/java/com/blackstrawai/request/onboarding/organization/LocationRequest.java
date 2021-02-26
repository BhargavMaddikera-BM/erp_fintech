package com.blackstrawai.request.onboarding.organization;

public class LocationRequest {
	
	private int id;
	private String name;
	private String gstNo;
	private String taxPanId;
	private String address_1;
	private String address_2;
	private int state;
	private String city;
	private int country;
	private String pinCode;
	private String status;
	private Boolean isMultiGST;
	public Boolean getIsMultiGST() {
		return isMultiGST;
	}
	public void setIsMultiGST(Boolean isMultiGST) {
		this.isMultiGST = isMultiGST;
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
	public String getGstNo() {
		return gstNo;
	}
	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}
	public String getTaxPanId() {
		return taxPanId;
	}
	public void setTaxPanId(String taxPanId) {
		this.taxPanId = taxPanId;
	}
	public String getAddress_1() {
		return address_1;
	}
	public void setAddress_1(String address_1) {
		this.address_1 = address_1;
	}
	public String getAddress_2() {
		return address_2;
	}
	public void setAddress_2(String address_2) {
		this.address_2 = address_2;
	}
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getCountry() {
		return country;
	}
	public void setCountry(int country) {
		this.country = country;
	}
	public String getPinCode() {
		return pinCode;
	}
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
