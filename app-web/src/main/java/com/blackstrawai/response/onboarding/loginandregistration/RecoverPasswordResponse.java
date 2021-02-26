package com.blackstrawai.response.onboarding.loginandregistration;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.onboarding.loginandregistration.RecoverPasswordVo;

public class RecoverPasswordResponse extends BaseResponse{
	
	private RecoverPasswordVo data;

	public RecoverPasswordVo getData() {
		return data;
	}

	public void setData(RecoverPasswordVo data) {
		this.data = data;
	}

}
