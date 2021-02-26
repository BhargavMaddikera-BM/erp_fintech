package com.blackstrawai.response.onboarding.user;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.onboarding.user.ListSentInvitesVo;

public class ListInvitesResponse extends BaseResponse{
	private List<ListSentInvitesVo> data;

	public List<ListSentInvitesVo> getData() {
		return data;
	}

	public void setData(List<ListSentInvitesVo> data) {
		this.data = data;
	}
	
	
}
