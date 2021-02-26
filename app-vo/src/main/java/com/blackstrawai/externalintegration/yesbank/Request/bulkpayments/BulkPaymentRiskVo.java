package com.blackstrawai.externalintegration.yesbank.Request.bulkpayments;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BulkPaymentRiskVo {

	@JsonProperty("PaymentContextCode")
	private String paymentContextCode;
	
	@JsonProperty("DeliveryAddress")
	private BulkPaymentDeliveryAddressVo deliveryAddress;


	public void setDeliveryAddress(BulkPaymentDeliveryAddressVo deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public void setPaymentContextCode(String paymentContextCode) {
		this.paymentContextCode = paymentContextCode;
	}

}
