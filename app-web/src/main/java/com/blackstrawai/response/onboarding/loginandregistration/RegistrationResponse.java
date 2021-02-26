package com.blackstrawai.response.onboarding.loginandregistration;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.onboarding.loginandregistration.RegistrationVo;

public class RegistrationResponse extends BaseResponse {
	
	private RegistrationVo data;

	public RegistrationVo getData() {
		return data;
	}

	public void setData(RegistrationVo data) {
		this.data = data;
	}

}
