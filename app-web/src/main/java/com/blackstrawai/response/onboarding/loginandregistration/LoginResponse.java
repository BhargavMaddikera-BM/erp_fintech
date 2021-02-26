package com.blackstrawai.response.onboarding.loginandregistration;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.TokenVo;

public class LoginResponse extends BaseResponse{
	
	private TokenVo data;
	private String type;
	private boolean isSuperAdmin=false;
	private String roleName;
	

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public boolean isSuperAdmin() {
		return isSuperAdmin;
	}

	public void setSuperAdmin(boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public TokenVo getData() {
		return data;
	}

	public void setData(TokenVo data) {
		this.data = data;
	}

}
