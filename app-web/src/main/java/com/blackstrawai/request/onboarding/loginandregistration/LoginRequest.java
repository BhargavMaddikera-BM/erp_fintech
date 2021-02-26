package com.blackstrawai.request.onboarding.loginandregistration;

import com.blackstrawai.common.BaseRequest;

public class LoginRequest extends BaseRequest{
	

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private String password;

}
