package com.blackstrawai.response.onboarding.loginandregistration;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.onboarding.loginandregistration.RegistrationVo;

public class ListRegistrationResponse extends BaseResponse {
	
	private List<RegistrationVo> data;

	public List<RegistrationVo> getData() {
		return data;
	}

	public void setData(List<RegistrationVo> data) {
		this.data = data;
	}


}
