package com.blackstrawai.response.onboarding.loginandregistration;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.onboarding.loginandregistration.SubscriptionVo;

public class SubscriptionResponse extends BaseResponse {
	
	private List<SubscriptionVo> data;

	public List<SubscriptionVo> getData() {
		return data;
	}

	public void setData(List<SubscriptionVo> data) {
		this.data = data;
	}

}
