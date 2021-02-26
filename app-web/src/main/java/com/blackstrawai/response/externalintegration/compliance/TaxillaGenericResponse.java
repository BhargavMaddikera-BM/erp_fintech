package com.blackstrawai.response.externalintegration.compliance;

import org.json.simple.JSONObject;

import com.blackstrawai.common.BaseResponse;

public class TaxillaGenericResponse extends BaseResponse {
	private JSONObject data;

	public JSONObject getData() {
		return data;
	}

	public void setData(JSONObject data) {
		this.data = data;
	}
}
