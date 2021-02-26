package com.blackstrawai.request.onboarding.loginandregistration;

import com.blackstrawai.common.BaseRequest;

public class ResetPasswordRequest extends BaseRequest{
	
	private String token;
	private String password;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
