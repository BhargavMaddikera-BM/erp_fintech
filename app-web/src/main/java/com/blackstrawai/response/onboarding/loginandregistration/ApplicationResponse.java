package com.blackstrawai.response.onboarding.loginandregistration;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.onboarding.loginandregistration.ApplicationsVo;

public class ApplicationResponse  extends BaseResponse {
	
	private ApplicationsVo data;

	public ApplicationsVo getData() {
		return data;
	}

	public void setData(ApplicationsVo data) {
		this.data = data;
	}

}
