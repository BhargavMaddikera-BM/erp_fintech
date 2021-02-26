package com.blackstrawai.externalintegration.yesbank.Request.bulkpayments;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BulkPaymentIdentitiesVo {
	@JsonProperty("MobileNumber")
	private String mobileNumber;

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
}
