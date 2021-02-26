package com.blackstrawai.externalintegration.yesbank.Response.bulkpayments;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BulkPaymentsDataResponse {


	  @JsonProperty("FilePaymentStatus")
	  private String  filePaymentStatus;

	  @JsonProperty("DateOfInitiation")
	  private String  dateOfInitiation;

	  @JsonProperty("FileIdentifier")
	  private String  fileIdentifier;

	  @JsonProperty("ConsentId")
	  private String  consentId;

	  @JsonProperty("SecondaryIdentification")
	  private String  secondaryIdentification;

	  @JsonProperty("NumberOfTransactions")
	  private String  numberOfTransactions;

	  @JsonProperty("ControSum")
	  private String  controSum;
	  
	  @JsonProperty("FileName")
	  private String  fileName;



	public String getFilePaymentStatus() {
		return filePaymentStatus;
	}

	public void setFilePaymentStatus(String filePaymentStatus) {
		this.filePaymentStatus = filePaymentStatus;
	}

	public String getDateOfInitiation() {
		return dateOfInitiation;
	}

	public void setDateOfInitiation(String dateOfInitiation) {
		this.dateOfInitiation = dateOfInitiation;
	}

	public String getFileIdentifier() {
		return fileIdentifier;
	}

	public void setFileIdentifier(String fileIdentifier) {
		this.fileIdentifier = fileIdentifier;
	}

	public String getConsentId() {
		return consentId;
	}

	public void setConsentId(String consentId) {
		this.consentId = consentId;
	}

	public String getSecondaryIdentification() {
		return secondaryIdentification;
	}

	public void setSecondaryIdentification(String secondaryIdentification) {
		this.secondaryIdentification = secondaryIdentification;
	}

	public String getNumberOfTransactions() {
		return numberOfTransactions;
	}

	public void setNumberOfTransactions(String numberOfTransactions) {
		this.numberOfTransactions = numberOfTransactions;
	}

	public String getControSum() {
		return controSum;
	}

	public void setControSum(String controSum) {
		this.controSum = controSum;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	  
	  
	  
}
