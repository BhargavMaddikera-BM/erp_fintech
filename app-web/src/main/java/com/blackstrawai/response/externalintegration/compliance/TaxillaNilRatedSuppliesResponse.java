package com.blackstrawai.response.externalintegration.compliance;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.compliance.taxilla.NilRatedSuppliesVo;

public class TaxillaNilRatedSuppliesResponse extends BaseResponse {
	private NilRatedSuppliesVo data;

	public NilRatedSuppliesVo getData() {
		return data;
	}

	public void setData(NilRatedSuppliesVo data) {
		this.data = data;
	}
	
	
}
