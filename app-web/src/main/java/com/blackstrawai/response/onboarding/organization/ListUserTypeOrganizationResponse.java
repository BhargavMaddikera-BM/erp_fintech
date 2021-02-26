package com.blackstrawai.response.onboarding.organization;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.onboarding.organization.UserTypeOrganizationVo;

public class ListUserTypeOrganizationResponse extends BaseResponse{
	private List<UserTypeOrganizationVo> data;

	public List<UserTypeOrganizationVo> getData() {
		return data;
	}

	public void setData(List<UserTypeOrganizationVo> data) {
		this.data = data;
	}

	
}
