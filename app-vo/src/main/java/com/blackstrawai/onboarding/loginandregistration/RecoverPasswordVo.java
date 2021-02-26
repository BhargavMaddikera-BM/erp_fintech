package com.blackstrawai.onboarding.loginandregistration;

import com.blackstrawai.common.TokenVo;

public class RecoverPasswordVo extends TokenVo {
	
	private String emailId;
	private String userToken;

	

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RecoverPasswordVo [emailId=");
		builder.append(emailId);
		builder.append(", userToken=");
		builder.append(userToken);
		builder.append("]");
		return builder.toString();
	}

	
	
}
