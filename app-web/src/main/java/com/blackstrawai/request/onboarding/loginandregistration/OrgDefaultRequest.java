package com.blackstrawai.request.onboarding.loginandregistration;

import com.blackstrawai.common.BaseRequest;

public class OrgDefaultRequest extends BaseRequest{
	
	  private String email;
	  private int organizationId;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}

}
