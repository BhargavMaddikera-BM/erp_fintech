package com.blackstrawai.response.ar.invoice;

import com.blackstrawai.ar.invoice.ArTaxComputationVo;
import com.blackstrawai.common.BaseResponse;

public class ArTaxComputationResponse extends BaseResponse{
	private ArTaxComputationVo data;

	public ArTaxComputationVo getData() {
		return data;
	}

	public void setData(ArTaxComputationVo data) {
		this.data = data;
	}
}
