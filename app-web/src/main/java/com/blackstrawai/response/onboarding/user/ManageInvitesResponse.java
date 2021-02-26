package com.blackstrawai.response.onboarding.user;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.onboarding.user.ManageInvitesVo;

public class ManageInvitesResponse extends BaseResponse{

	private List<ManageInvitesVo> data;

	public List<ManageInvitesVo> getData() {
		return data;
	}

	public void setData(List<ManageInvitesVo> data) {
		this.data = data;
	}
	
	

}
