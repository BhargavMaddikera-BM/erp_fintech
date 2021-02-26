package com.blackstrawai.response.externalintegration.general;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.general.akshar.AksharProductItemVo;

public class AksharProductResponse extends BaseResponse {

	private List<AksharProductItemVo> data;

	public List<AksharProductItemVo> getData() {
		return data;
	}

	public void setData(List<AksharProductItemVo> data) {
		this.data = data;
	}

	
}
