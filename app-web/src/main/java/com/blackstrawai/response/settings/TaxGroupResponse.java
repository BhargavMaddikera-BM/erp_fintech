package com.blackstrawai.response.settings;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.TaxGroupVo;

public class TaxGroupResponse extends BaseResponse{
	
	private TaxGroupVo data;

	public TaxGroupVo getData() {
		return data;
	}

	public void setData(TaxGroupVo data) {
		this.data = data;
	}
	

}
