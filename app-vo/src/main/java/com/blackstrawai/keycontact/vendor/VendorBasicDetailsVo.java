package com.blackstrawai.keycontact.vendor;

public class VendorBasicDetailsVo {
	
	private String vendorDisplayName;
	private String primaryContact;
	private String phoneNumber;
	private String mobileNumber;
	private Integer noOfOpenPo;
	private Integer vendorId;
	private String status;
	
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getVendorId() {
		return vendorId;
	}
	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}
	public String getVendorDisplayName() {
		return vendorDisplayName;
	}
	public void setVendorDisplayName(String vendorDisplayName) {
		this.vendorDisplayName = vendorDisplayName;
	}
	public String getPrimaryContact() {
		return primaryContact;
	}
	public void setPrimaryContact(String primaryContact) {
		this.primaryContact = primaryContact;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public Integer getNoOfOpenPo() {
		return noOfOpenPo;
	}
	public void setNoOfOpenPo(Integer noOfOpenPo) {
		this.noOfOpenPo = noOfOpenPo;
	}

	
}
