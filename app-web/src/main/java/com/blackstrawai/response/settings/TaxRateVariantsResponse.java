package com.blackstrawai.response.settings;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.TaxRateVariantsVo;

public class TaxRateVariantsResponse extends BaseResponse{
	
	private TaxRateVariantsVo data;

	public TaxRateVariantsVo getData() {
		return data;
	}

	public void setData(TaxRateVariantsVo data) {
		this.data = data;
	}
	

}
