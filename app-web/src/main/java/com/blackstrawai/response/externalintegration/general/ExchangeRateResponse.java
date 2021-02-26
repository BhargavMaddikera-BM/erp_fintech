package com.blackstrawai.response.externalintegration.general;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.general.exchangerate.ExchangeRateVo;

public class ExchangeRateResponse extends BaseResponse{

	private ExchangeRateVo data;

	public ExchangeRateVo getData() {
		return data;
	}

	public void setData(ExchangeRateVo data) {
		this.data = data;
	}
	
	
}
