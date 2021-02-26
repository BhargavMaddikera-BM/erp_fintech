package com.blackstrawai.response.ar.applycredits;

import com.blackstrawai.ar.applycredits.ApplyCreditsVo;
import com.blackstrawai.common.BaseResponse;

public class ApplyCreditsResponse extends BaseResponse {

	private ApplyCreditsVo data;

	public ApplyCreditsVo getData() {
		return data;
	}

	public void setData(ApplyCreditsVo data) {
		this.data = data;
	}

}
