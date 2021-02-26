package com.blackstrawai.response.externalintegration.general;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.general.instafinancials.InstaFinancialsVo;

public class InstaFinancialsResponse extends BaseResponse{

	private InstaFinancialsVo data;

	public InstaFinancialsVo getData() {
		return data;
	}

	public void setData(InstaFinancialsVo data) {
		this.data = data;
	}
	
	
}
