package com.blackstrawai.response.onboarding.role;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.onboarding.role.RoleVo;

public class RoleResponse extends BaseResponse{
	private RoleVo data;

	public RoleVo getData() {
		return data;
	}

	public void setData(RoleVo data) {
		this.data = data;
	}

}
