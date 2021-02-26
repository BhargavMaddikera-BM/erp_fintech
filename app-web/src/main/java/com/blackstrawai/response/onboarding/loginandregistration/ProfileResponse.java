package com.blackstrawai.response.onboarding.loginandregistration;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.onboarding.loginandregistration.ProfileVo;

public class ProfileResponse extends BaseResponse {
	
	private ProfileVo data;

	public ProfileVo getData() {
		return data;
	}

	public void setData(ProfileVo data) {
		this.data = data;
	}

}
