package com.blackstrawai.response.ap.billsinvoice;

import com.blackstrawai.ap.billsinvoice.TaxComputationVo;
import com.blackstrawai.common.BaseResponse;

public class TaxComputationResponse extends BaseResponse{
	private TaxComputationVo data;

	public TaxComputationVo getData() {
		return data;
	}

	public void setData(TaxComputationVo data) {
		this.data = data;
	}
}
