package com.blackstrawai.response.externalintegration.banking.yesbank;

import java.util.Map;

import com.blackstrawai.common.BaseResponse;

public class BulkPaymentStatusResponse extends BaseResponse{

	
	private Map<String , String > data;

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	
}
