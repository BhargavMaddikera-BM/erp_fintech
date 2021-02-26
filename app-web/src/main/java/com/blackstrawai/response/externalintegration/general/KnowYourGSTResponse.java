package com.blackstrawai.response.externalintegration.general;

import java.util.List;

import com.blackstrawai.common.BaseResponse;

public class KnowYourGSTResponse extends BaseResponse{
	
	private List<String>data;

	public List<String> getData() {
		return data;
	}

	public void setData(List<String> data) {
		this.data = data;
	}

}
