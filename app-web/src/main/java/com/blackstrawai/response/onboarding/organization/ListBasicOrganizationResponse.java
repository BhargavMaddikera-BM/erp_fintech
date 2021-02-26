package com.blackstrawai.response.onboarding.organization;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.onboarding.organization.BasicOrganizationVo;

public class ListBasicOrganizationResponse extends BaseResponse{
	
	private List<BasicOrganizationVo> data;

	public List<BasicOrganizationVo> getData() {
		return data;
	}

	public void setData(List<BasicOrganizationVo> data) {
		this.data = data;
	}

}
