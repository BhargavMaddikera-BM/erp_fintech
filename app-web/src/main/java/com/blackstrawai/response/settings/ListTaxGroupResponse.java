package com.blackstrawai.response.settings;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.TaxGroupVo;

public class ListTaxGroupResponse extends BaseResponse{

	private List<TaxGroupVo> data;

	public List<TaxGroupVo> getData() {
		return data;
	}

	public void setData(List<TaxGroupVo> data) {
		this.data = data;
	}

	
	
}
