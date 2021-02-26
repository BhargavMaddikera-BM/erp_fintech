package com.blackstrawai.externalintegration.yesbank.Request.bulkpayments;


import com.fasterxml.jackson.annotation.JsonProperty;

public class BulkPaymentCreditorUnstructuredVo {

	
	@JsonProperty("ContactInformation")
	private String contactInformation;
	
	@JsonProperty("Identities")
	private String identities;

	public String getContactInformation() {
		return contactInformation;
	}

	public void setContactInformation(String contactInformation) {
		this.contactInformation = contactInformation;
	}

	public String getIdentities() {
		return identities;
	}

	public void setIdentities(String identities) {
		this.identities = identities;
	}

	
	
}
