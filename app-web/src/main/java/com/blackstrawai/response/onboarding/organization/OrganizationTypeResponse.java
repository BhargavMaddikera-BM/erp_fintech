package com.blackstrawai.response.onboarding.organization;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.onboarding.organization.OrganizationTypeVo;

public class OrganizationTypeResponse extends BaseResponse{
	
	private List<OrganizationTypeVo> data;

	public List<OrganizationTypeVo> getData() {
		return data;
	}

	public void setData(List<OrganizationTypeVo> data) {
		this.data = data;
	}

	

	
}
