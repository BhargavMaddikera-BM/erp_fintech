package com.blackstrawai.response.settings;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.TaxRateMappingVo;

public class ListTaxRateMappingResponse extends BaseResponse{

	private List<TaxRateMappingVo> data;

	public List<TaxRateMappingVo> getData() {
		return data;
	}

	public void setData(List<TaxRateMappingVo> data) {
		this.data = data;
	}

	
	
}
