package com.blackstrawai.onboarding.organization;

import com.blackstrawai.common.BaseVo;

public class OrganizationPartnershipPartnerVo extends BaseVo{
	
	private int id;
	private String name;
	private String emailId;
	private String mobileNo;
	private String ownershipPercentage;
	private String status;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getOwnershipPercentage() {
		return ownershipPercentage;
	}
	public void setOwnershipPercentage(String ownershipPercentage) {
		this.ownershipPercentage = ownershipPercentage;
	}

}
