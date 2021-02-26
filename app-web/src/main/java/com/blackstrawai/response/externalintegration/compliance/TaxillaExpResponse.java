package com.blackstrawai.response.externalintegration.compliance;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.compliance.taxilla.ExpVo;

public class TaxillaExpResponse extends BaseResponse {
	private List<ExpVo> data;

	public List<ExpVo> getData() {
		return data;
	}

	public void setData(List<ExpVo> data) {
		this.data = data;
	}
	
	
}
