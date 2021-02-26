package com.blackstrawai.externalintegration.yesbank.Request.bulkpayments;


import com.fasterxml.jackson.annotation.JsonProperty;

public class BulkPaymentUnstructuredVo {

	
	@JsonProperty("ContactInformation")
	private BulkPaymentContactInformationVo contactInformation;
	
	@JsonProperty("Identities")
	private BulkPaymentIdentitiesVo identities;

	public BulkPaymentContactInformationVo getContactInformation() {
		return contactInformation;
	}

	public void setContactInformation(BulkPaymentContactInformationVo contactInformation) {
		this.contactInformation = contactInformation;
	}

	public BulkPaymentIdentitiesVo getIdentities() {
		return identities;
	}

	public void setIdentities(BulkPaymentIdentitiesVo identities) {
		this.identities = identities;
	}

	
	
}
