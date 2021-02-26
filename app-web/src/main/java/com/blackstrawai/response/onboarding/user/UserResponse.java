package com.blackstrawai.response.onboarding.user;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.onboarding.user.UserVo;

public class UserResponse extends BaseResponse{
	private UserVo data;

	public UserVo getData() {
		return data;
	}

	public void setData(UserVo data) {
		this.data = data;
	}

}
