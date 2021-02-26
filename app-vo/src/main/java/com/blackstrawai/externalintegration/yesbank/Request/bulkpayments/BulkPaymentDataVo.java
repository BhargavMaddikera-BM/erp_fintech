package com.blackstrawai.externalintegration.yesbank.Request.bulkpayments;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BulkPaymentDataVo {

	@JsonProperty("FileIdentifier")
	private String fileIdentifier ;
	
	@JsonProperty("NumberOfTransactions")
	private String  numberOfTransactions ;
	
	@JsonProperty("ConsentId")
	private String consentId ;
	
	@JsonProperty("ControSum")
	private String controSum ;
	
	@JsonProperty("SecondaryIdentification")
	private String secondaryIdentification ;
	
	@JsonProperty("DomesticPayments")
	private List<BulkPaymentDomesticPaymentVo> domesticPayments ;

	public String getFileIdentifier() {
		return fileIdentifier;
	}

	public void setFileIdentifier(String fileIdentifier) {
		this.fileIdentifier = fileIdentifier;
	}

	public String getNumberOfTransactions() {
		return numberOfTransactions;
	}

	public void setNumberOfTransactions(String numberOfTransactions) {
		this.numberOfTransactions = numberOfTransactions;
	}

	public String getConsentId() {
		return consentId;
	}

	public void setConsentId(String consentId) {
		this.consentId = consentId;
	}

	public String getControSum() {
		return controSum;
	}

	public void setControSum(String controSum) {
		this.controSum = controSum;
	}

	public String getSecondaryIdentification() {
		return secondaryIdentification;
	}

	public void setSecondaryIdentification(String secondaryIdentification) {
		this.secondaryIdentification = secondaryIdentification;
	}

	public List<BulkPaymentDomesticPaymentVo> getDomesticPayments() {
		return domesticPayments;
	}

	public void setDomesticPayments(List<BulkPaymentDomesticPaymentVo> domesticPayments) {
		this.domesticPayments = domesticPayments;
	}

	@Override
	public String toString() {
		return "BulkPaymentDataVo [fileIdentifier=" + fileIdentifier + ", numberOfTransactions=" + numberOfTransactions
				+ ", consentId=" + consentId + ", controSum=" + controSum + ", secondaryIdentification="
				+ secondaryIdentification + ", domesticPayments=" + domesticPayments + "]";
	}
	
	
}
