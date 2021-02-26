package com.blackstrawai.response.onboarding.user;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.onboarding.user.UserVo;

public class ListUserResponse extends BaseResponse{

	private List<UserVo>data;

	public List<UserVo> getData() {
		return data;
	}

	public void setData(List<UserVo> data) {
		this.data = data;
	}

}
