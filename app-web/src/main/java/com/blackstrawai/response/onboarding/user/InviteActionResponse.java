package com.blackstrawai.response.onboarding.user;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.onboarding.user.InviteActionVo;

public class InviteActionResponse extends BaseResponse {
	private InviteActionVo data;

	public InviteActionVo getData() {
		return data;
	}

	public void setData(InviteActionVo data) {
		this.data = data;
	}
	
	
}
