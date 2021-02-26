package com.blackstrawai.response.settings;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.BaseCurrencyVo;

public class BaseCurrencyResponse extends BaseResponse{
	
	private List<BaseCurrencyVo> data;

	public List<BaseCurrencyVo> getData() {
		return data;
	}

	public void setData(List<BaseCurrencyVo> data) {
		this.data = data;
	}

	
}
