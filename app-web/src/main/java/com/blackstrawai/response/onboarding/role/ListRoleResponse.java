package com.blackstrawai.response.onboarding.role;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.onboarding.role.RoleVo;

public class ListRoleResponse extends BaseResponse{
	
	private List<RoleVo>data;

	public List<RoleVo> getData() {
		return data;
	}

	public void setData(List<RoleVo> data) {
		this.data = data;
	}

}
