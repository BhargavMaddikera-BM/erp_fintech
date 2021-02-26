package com.blackstrawai.response.onboarding.organization;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.onboarding.organization.IncomeTaxLoginVo;

public class IncomeTaxLoginResponse extends BaseResponse{

	private IncomeTaxLoginVo data;

	public IncomeTaxLoginVo getData() {
		return data;
	}

	public void setData(IncomeTaxLoginVo data) {
		this.data = data;
	}
}