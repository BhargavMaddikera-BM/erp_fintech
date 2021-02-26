package com.blackstrawai.response.settings;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.ShippingPreferenceVo;

public class ListshippingPreferenceResponse extends BaseResponse{

	
	private List<ShippingPreferenceVo> data;

	public List<ShippingPreferenceVo> getData() {
		return data;
	}

	public void setData(List<ShippingPreferenceVo> data) {
		this.data = data;
	}
	
	
	
}
