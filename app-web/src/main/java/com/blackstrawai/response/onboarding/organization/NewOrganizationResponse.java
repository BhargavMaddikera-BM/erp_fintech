package com.blackstrawai.response.onboarding.organization;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.onboarding.organization.BasicOrganizationVo;

public class NewOrganizationResponse extends BaseResponse{
	private BasicOrganizationVo data;

	public BasicOrganizationVo getData() {
		return data;
	}

	public void setData(BasicOrganizationVo data) {
		this.data = data;
	}

	

	
}
