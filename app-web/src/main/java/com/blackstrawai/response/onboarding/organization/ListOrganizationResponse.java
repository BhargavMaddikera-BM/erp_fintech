package com.blackstrawai.response.onboarding.organization;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.onboarding.organization.OrganizationVo;

public class ListOrganizationResponse extends BaseResponse{
	private List<OrganizationVo> data;

	public List<OrganizationVo> getData() {
		return data;
	}

	public void setData(List<OrganizationVo> data) {
		this.data = data;
	}

	

}
