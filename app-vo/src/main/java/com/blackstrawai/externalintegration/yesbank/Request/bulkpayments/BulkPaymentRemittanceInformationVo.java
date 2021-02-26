package com.blackstrawai.externalintegration.yesbank.Request.bulkpayments;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BulkPaymentRemittanceInformationVo {

	@JsonProperty("Unstructured")
	  private BulkPaymentRemittanceUnstructuredVo unstructured;

	public void setUnstructured(BulkPaymentRemittanceUnstructuredVo unstructured) {
		this.unstructured = unstructured;
	}

	public BulkPaymentRemittanceUnstructuredVo getUnstructured() {
		return unstructured;
	}
	

}
