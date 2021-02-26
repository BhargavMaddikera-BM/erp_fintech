package com.blackstrawai.response.settings;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.TaxRateTypeVo;

public class ListTaxRateTypeResponse extends BaseResponse{

	private List<TaxRateTypeVo> data;

	public List<TaxRateTypeVo> getData() {
		return data;
	}

	public void setData(List<TaxRateTypeVo> data) {
		this.data = data;
	}

	
	
}
