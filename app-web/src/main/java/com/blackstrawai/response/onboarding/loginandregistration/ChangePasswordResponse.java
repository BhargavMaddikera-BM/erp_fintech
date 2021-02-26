package com.blackstrawai.response.onboarding.loginandregistration;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.onboarding.loginandregistration.ChangePasswordVo;

public class ChangePasswordResponse extends BaseResponse{
	
	private ChangePasswordVo data;

	public ChangePasswordVo getData() {
		return data;
	}

	public void setData(ChangePasswordVo data) {
		this.data = data;
	}

}
