package com.blackstrawai.response.settings;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.CurrencyVo;

public class ListCurrencyResponse extends BaseResponse{
	
	private List<CurrencyVo>data;

	public List<CurrencyVo> getData() {
		return data;
	}

	public void setData(List<CurrencyVo> data) {
		this.data = data;
	}

	
}
