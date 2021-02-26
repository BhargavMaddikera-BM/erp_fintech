package com.blackstrawai.response.onboarding.organization;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.onboarding.organization.OrganizationConstitutionVo;

public class OrganizationConstitutionResponse extends BaseResponse{
	
	private List<OrganizationConstitutionVo> data;

	public List<OrganizationConstitutionVo> getData() {
		return data;
	}

	public void setData(List<OrganizationConstitutionVo> data) {
		this.data = data;
	}

	
}
