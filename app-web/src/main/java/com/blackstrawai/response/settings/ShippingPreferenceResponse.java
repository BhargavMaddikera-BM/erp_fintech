package com.blackstrawai.response.settings;



import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.ShippingPreferenceVo;

public class ShippingPreferenceResponse extends BaseResponse{

	private ShippingPreferenceVo data;

	public ShippingPreferenceVo getData() {
		return data;
	}

	public void setData(ShippingPreferenceVo data) {
		this.data = data;
	}
	
}
