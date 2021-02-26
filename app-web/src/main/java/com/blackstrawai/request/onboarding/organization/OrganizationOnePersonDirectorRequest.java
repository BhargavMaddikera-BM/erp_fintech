package com.blackstrawai.request.onboarding.organization;

import com.blackstrawai.common.BaseRequest;

public class OrganizationOnePersonDirectorRequest extends BaseRequest{
	
	private int id;
	private String name;
	private String emailId;
	private String mobileNo;
	private String din;
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
	public String getDin() {
		return din;
	}
	public void setDin(String din) {
		this.din = din;
	}

}
