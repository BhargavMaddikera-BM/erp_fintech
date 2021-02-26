package com.blackstrawai.response.onboarding.organization;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.onboarding.organization.OrganizationIndustryVo;

public class OrganizationIndustryResponse extends BaseResponse{
	
	private List<OrganizationIndustryVo> data;

	public List<OrganizationIndustryVo> getData() {
		return data;
	}

	public void setData(List<OrganizationIndustryVo> data) {
		this.data = data;
	}

	

}
