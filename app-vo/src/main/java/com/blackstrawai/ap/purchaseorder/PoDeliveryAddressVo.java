package com.blackstrawai.ap.purchaseorder;

public class PoDeliveryAddressVo {
	
	private String address_1;
	
	private String address_2;
	
	private String city;
	
	private Integer country;
	
	private Integer state;
	
	private String zipCode;
	
	private String landMark;
	
	private String phoneNo;
	
	private String attention;
	

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getAttention() {
		return attention;
	}

	public void setAttention(String attention) {
		this.attention = attention;
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

	public Integer getCountry() {
		return country;
	}

	public void setCountry(Integer country) {
		this.country = country;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getLandMark() {
		return landMark;
	}

	public void setLandMark(String landMark) {
		this.landMark = landMark;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PoDeliveryAddressVo [address_1=");
		builder.append(address_1);
		builder.append(", address_2=");
		builder.append(address_2);
		builder.append(", city=");
		builder.append(city);
		builder.append(", country=");
		builder.append(country);
		builder.append(", state=");
		builder.append(state);
		builder.append(", zipCode=");
		builder.append(zipCode);
		builder.append(", landMark=");
		builder.append(landMark);
		builder.append(", phoneNum=");
		builder.append(phoneNo);
		builder.append("]");
		return builder.toString();
	}
	
	
}
