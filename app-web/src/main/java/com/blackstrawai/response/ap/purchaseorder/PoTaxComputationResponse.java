package com.blackstrawai.response.ap.purchaseorder;

import com.blackstrawai.ap.purchaseorder.PoTaxComputationVo;
import com.blackstrawai.common.BaseResponse;

public class PoTaxComputationResponse extends BaseResponse{
	private PoTaxComputationVo data;

	public PoTaxComputationVo getData() {
		return data;
	}

	public void setData(PoTaxComputationVo data) {
		this.data = data;
	}
}
