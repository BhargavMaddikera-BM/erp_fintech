package com.blackstrawai.response.onboarding.user;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.onboarding.user.InviteUserVo;

public class InviteUserResponse extends BaseResponse{
	private InviteUserVo data;

	public InviteUserVo getData() {
		return data;
	}

	public void setData(InviteUserVo data) {
		this.data = data;
	}
}
