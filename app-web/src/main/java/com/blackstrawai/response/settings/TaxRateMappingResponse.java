package com.blackstrawai.response.settings;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.TaxRateMappingVo;

public class TaxRateMappingResponse extends BaseResponse{
	
	private TaxRateMappingVo data;

	public TaxRateMappingVo getData() {
		return data;
	}

	public void setData(TaxRateMappingVo data) {
		this.data = data;
	}
	

}
