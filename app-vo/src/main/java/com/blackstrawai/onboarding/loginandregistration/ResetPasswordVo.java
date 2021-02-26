package com.blackstrawai.onboarding.loginandregistration;

import com.blackstrawai.common.TokenVo;

public class ResetPasswordVo extends TokenVo{
	private String resetToken;
	private String password;
	public String getResetToken() {
		return resetToken;
	}
	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
