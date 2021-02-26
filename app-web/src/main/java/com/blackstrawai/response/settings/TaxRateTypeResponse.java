package com.blackstrawai.response.settings;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.TaxRateTypeVo;

public class TaxRateTypeResponse extends BaseResponse{
	
	private TaxRateTypeVo data;

	public TaxRateTypeVo getData() {
		return data;
	}

	public void setData(TaxRateTypeVo data) {
		this.data = data;
	}
	

}
