package com.blackstrawai.externalintegration.yesbank.Request.bulkpayments;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BulkPaymentDeliveryAddressVo {

	@JsonProperty("AddressLine")
	private List<String> addressLine;

	@JsonProperty("Country")
	private String country;

	public void setAddressLine(List<String> addressLine) {
		this.addressLine = addressLine;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
