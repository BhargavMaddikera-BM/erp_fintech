package com.blackstrawai.response.onboarding.loginandregistration;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.onboarding.loginandregistration.ResetPasswordVo;

public class ResetPasswordResponse extends BaseResponse{
	
	private ResetPasswordVo data;

	public ResetPasswordVo getData() {
		return data;
	}

	public void setData(ResetPasswordVo data) {
		this.data = data;
	}

}
