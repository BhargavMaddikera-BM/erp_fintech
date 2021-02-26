package com.blackstrawai.response.externalintegration.compliance;

import java.util.Map;

import com.blackstrawai.common.BaseResponse;

public class TaxillaStatusResponse extends BaseResponse {
	private Map<String, String> data;

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String > data) {
		this.data = data;
	}
	
	
}
