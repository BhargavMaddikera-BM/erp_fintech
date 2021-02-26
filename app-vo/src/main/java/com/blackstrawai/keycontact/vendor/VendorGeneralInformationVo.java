package com.blackstrawai.keycontact.vendor;

import java.sql.Timestamp;

public class VendorGeneralInformationVo {

	private Integer id;
	private String primaryContact;
	private String companyName;
	private String vendorDisplayName;
	private String email;
	private String phoneNo;
	private String website;
	private String mobileNo;
	private String pan;
	private Integer vendorOrganizationId;
	private Integer vendorGroupId;
	private Integer vendorGstTypeId;
	private String gstNo;
	private String status;
	private Integer poCount;
	private Timestamp createTs;
	private Timestamp updateTs;
	private Boolean isMsmeRegistered;
	private String msmeNumber;
	private String[] otherGsts;
	private Boolean isPanOrGstAvailable;
	private Boolean overSeasVendor;
	private Boolean vendorWithoutPan;

	public Boolean getIsMsmeRegistered() {
		return isMsmeRegistered;
	}

	public void setIsMsmeRegistered(Boolean isMsmeRegistered) {
		this.isMsmeRegistered = isMsmeRegistered;
	}

	public String getMsmeNumber() {
		return msmeNumber;
	}

	public void setMsmeNumber(String msmeNumber) {
		this.msmeNumber = msmeNumber;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPrimaryContact() {
		return primaryContact;
	}

	public void setPrimaryContact(String primaryContact) {
		this.primaryContact = primaryContact;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getVendorDisplayName() {
		return vendorDisplayName;
	}

	public void setVendorDisplayName(String vendorDisplayName) {
		this.vendorDisplayName = vendorDisplayName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public Integer getVendorOrganizationId() {
		return vendorOrganizationId;
	}

	public void setVendorOrganizationId(Integer vendorOrganizationId) {
		this.vendorOrganizationId = vendorOrganizationId;
	}

	public Integer getVendorGroupId() {
		return vendorGroupId;
	}

	public void setVendorGroupId(Integer vendorGroupId) {
		this.vendorGroupId = vendorGroupId;
	}

	public Integer getVendorGstTypeId() {
		return vendorGstTypeId;
	}

	public void setVendorGstTypeId(Integer vendorGstTypeId) {
		this.vendorGstTypeId = vendorGstTypeId;
	}

	public String getGstNo() {
		return gstNo;
	}

	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getPoCount() {
		return poCount;
	}

	public void setPoCount(Integer poCount) {
		this.poCount = poCount;
	}

	public Timestamp getCreateTs() {
		return createTs;
	}

	public void setCreateTs(Timestamp createTs) {
		this.createTs = createTs;
	}

	public Timestamp getUpdateTs() {
		return updateTs;
	}

	public void setUpdateTs(Timestamp updateTs) {
		this.updateTs = updateTs;
	}


	public String[] getOtherGsts() {
		return otherGsts;
	}

	public void setOtherGsts(String[] otherGsts) {
		this.otherGsts = otherGsts;
	}

	public Boolean getIsPanOrGstAvailable() {
		return isPanOrGstAvailable;
	}

	public Boolean getOverSeasVendor() {
		return overSeasVendor;
	}

	public Boolean getVendorWithoutPan() {
		return vendorWithoutPan;
	}

	public void setIsPanOrGstAvailable(Boolean isPanOrGstAvailable) {
		this.isPanOrGstAvailable = isPanOrGstAvailable;
	}

	public void setOverSeasVendor(Boolean overSeasVendor) {
		this.overSeasVendor = overSeasVendor;
	}

	public void setVendorWithoutPan(Boolean vendorWithoutPan) {
		this.vendorWithoutPan = vendorWithoutPan;
	}
}
