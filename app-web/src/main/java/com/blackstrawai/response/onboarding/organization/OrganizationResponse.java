package com.blackstrawai.response.onboarding.organization;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.onboarding.organization.NewOrganizationVo;

public class OrganizationResponse extends BaseResponse{
	private NewOrganizationVo data;

	public NewOrganizationVo getData() {
		return data;
	}

	public void setData(NewOrganizationVo data) {
		this.data = data;
	}

	

}
