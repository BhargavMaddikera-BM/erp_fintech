package com.blackstrawai.externalintegration.yesbank.Request.bulkpayments;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BulkPaymentDomesticPaymentVo {

	@JsonProperty("ConsentId")
	private String consentId ;
	
	@JsonProperty("Initiation")
	private BulkPaymentInitiationVo initiation ;
	
	@JsonProperty("Risk")
	private BulkPaymentRiskVo risk ;

	public String getConsentId() {
		return consentId;
	}

	public void setConsentId(String consentId) {
		this.consentId = consentId;
	}

	public BulkPaymentInitiationVo getInitiation() {
		return initiation;
	}

	public void setInitiation(BulkPaymentInitiationVo initiation) {
		this.initiation = initiation;
	}

	public BulkPaymentRiskVo getRisk() {
		return risk;
	}

	public void setRisk(BulkPaymentRiskVo risk) {
		this.risk = risk;
	}
	
	
}
