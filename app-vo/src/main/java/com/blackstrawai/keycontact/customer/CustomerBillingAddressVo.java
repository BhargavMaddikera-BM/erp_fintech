package com.blackstrawai.keycontact.customer;

public class CustomerBillingAddressVo {

	
	private String attention;
	
	private String phoneNo;
	
	private Integer country;
	
	private String countryName;
	
	private String address_1;
	
	private String address_2;
	
	private String landMark;
	
	private Integer state;
	
	private String stateName;
	
	private String city ;
	
	private String zipCode;
	
	private Boolean isSameOriginDestAddress;
	
	private Integer addressType;
	
	private Integer selectedAddress;
	
    private String gstNo;
	
	public String getAttention() {
		return attention;
	}

	public void setAttention(String attention) {
		this.attention = attention;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}


	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
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

	public String getLandMark() {
		return landMark;
	}

	public void setLandMark(String landMark) {
		this.landMark = landMark;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public Boolean getIsSameOriginDestAddress() {
		return isSameOriginDestAddress;
	}

	public void setIsSameOriginDestAddress(Boolean isSameOriginDestAddress) {
		this.isSameOriginDestAddress = isSameOriginDestAddress;
	}
	

	public Integer getSelectedAddress() {
		return selectedAddress;
	}

	public void setSelectedAddress(Integer selectedAddress) {
		this.selectedAddress = selectedAddress;
	}

	public String getGstNo() {
		return gstNo;
	}

	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}
	
	

	public Integer getAddressType() {
		return addressType;
	}

	public void setAddressType(Integer addressType) {
		this.addressType = addressType;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CustomerBillingAddressVo [attention=");
		builder.append(attention);
		builder.append(", phoneNo=");
		builder.append(phoneNo);
		builder.append(", country=");
		builder.append(country);
		builder.append(", address_1=");
		builder.append(address_1);
		builder.append(", address_2=");
		builder.append(address_2);
		builder.append(", landmark=");
		builder.append(landMark);
		builder.append(", state=");
		builder.append(state);
		builder.append(", city=");
		builder.append(city);
		builder.append(", zipCode=");
		builder.append(zipCode);
		builder.append(", isSameOriginDestAddress=");
		builder.append(isSameOriginDestAddress);
		builder.append("]");
		return builder.toString();
	}
	
	
	
	
}
