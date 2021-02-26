package com.blackstrawai.externalintegration.yesbank.Request.bulkpayments;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BulkPaymentVo {

	@JsonProperty("Data")
	private BulkPaymentDataVo data ;

	public BulkPaymentDataVo getData() {
		return data;
	}

	public void setData(BulkPaymentDataVo data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "BulkPaymentVo [data=" + data + "]";
	}
	
	
	
}
