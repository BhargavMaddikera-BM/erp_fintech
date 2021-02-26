package com.blackstrawai.response.onboarding.user;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.onboarding.user.WithdrawInviteVo;

public class WithdrawInviteResponse extends BaseResponse {
	private WithdrawInviteVo data;

	public WithdrawInviteVo getData() {
		return data;
	}

	public void setData(WithdrawInviteVo data) {
		this.data = data;
	}
}
