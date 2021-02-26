package com.blackstrawai.response.ar.applycredits;

import java.util.List;

import com.blackstrawai.ar.applycredits.ApplyCreditsBasicVo;
import com.blackstrawai.common.BaseResponse;

public class ApplyCreditsListResponse extends BaseResponse {

	private List<ApplyCreditsBasicVo> data;

	public List<ApplyCreditsBasicVo> getData() {
		return data;
	}

	public void setData(List<ApplyCreditsBasicVo> data) {
		this.data = data;
	}

}
