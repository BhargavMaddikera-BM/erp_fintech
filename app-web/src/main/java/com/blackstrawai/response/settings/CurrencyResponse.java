package com.blackstrawai.response.settings;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.CurrencyVo;

public class CurrencyResponse extends BaseResponse{
	private CurrencyVo data;

	public CurrencyVo getData() {
		return data;
	}

	public void setData(CurrencyVo data) {
		this.data = data;
	}

	
}
