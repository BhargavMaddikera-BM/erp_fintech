package com.blackstrawai.externalintegration.yesbank.Request.bulkpayments;


import com.fasterxml.jackson.annotation.JsonProperty;

public class BulkPaymentCreditorAccountVo {

	@JsonProperty("SchemeName")
	private String schemeName;
	
	@JsonProperty("Identification")
	private String identification;
	
	@JsonProperty("Name")
	private String name;
	
	@JsonProperty("Unstructured")
	private BulkPaymentUnstructuredVo unstructured;

	public String getSchemeName() {
		return schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BulkPaymentUnstructuredVo getUnstructured() {
		return unstructured;
	}

	public void setUnstructured(BulkPaymentUnstructuredVo unstructured) {
		this.unstructured = unstructured;
	}
	
	
}
