package com.blackstrawai.response.externalintegration.compliance;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.compliance.taxilla.TaxillaVo;

public class TaxillaResponse extends BaseResponse {

	private TaxillaVo data;

	public TaxillaVo getData() {
		return data;
	}

	public void setData(TaxillaVo data) {
		this.data = data;
	}

	}
