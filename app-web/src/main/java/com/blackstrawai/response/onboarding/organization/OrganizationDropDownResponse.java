package com.blackstrawai.response.onboarding.organization;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.onboarding.organization.OrganizationDropDownVo;

public class OrganizationDropDownResponse extends BaseResponse{
	
	private OrganizationDropDownVo data;

	public OrganizationDropDownVo getData() {
		return data;
	}

	public void setData(OrganizationDropDownVo data) {
		this.data = data;
	}
	

}
