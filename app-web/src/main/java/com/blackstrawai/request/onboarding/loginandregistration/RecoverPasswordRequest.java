package com.blackstrawai.request.onboarding.loginandregistration;

import com.blackstrawai.common.BaseRequest;

public class RecoverPasswordRequest extends BaseRequest{
	
	String emailId;

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

}
