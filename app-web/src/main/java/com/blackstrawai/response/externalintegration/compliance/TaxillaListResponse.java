package com.blackstrawai.response.externalintegration.compliance;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.compliance.taxilla.TaxillaVo;

public class TaxillaListResponse extends BaseResponse {

	private List<TaxillaVo> data;

	public List<TaxillaVo> getData() {
		return data;
	}

	public void setData(List<TaxillaVo> data) {
		this.data = data;
	}

}
