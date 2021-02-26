package com.blackstrawai.response.onboarding.organization;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.onboarding.organization.MinimalOrganizationVo;

public class NewMinimalOrganizationResponse extends BaseResponse{

	private MinimalOrganizationVo data;

	public MinimalOrganizationVo getData() {
		return data;
	}

	public void setData(MinimalOrganizationVo data) {
		this.data = data;
	}
}
