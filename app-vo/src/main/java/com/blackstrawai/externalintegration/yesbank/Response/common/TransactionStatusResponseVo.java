package com.blackstrawai.externalintegration.yesbank.Response.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionStatusResponseVo {

  @JsonProperty("ConsentId")
  private String consentId;
  @JsonProperty("CreationDateTime")
  private String creationDateTime;
  @JsonProperty("Status")
  private String status;
  @JsonProperty("StatusUpdateDateTime")
  private String statusUpdateDateTime;
  @JsonProperty("TransactionIdentification")
  private String transactionIdentification;

  public String getConsentId() {
    return consentId;
  }

  public void setConsentId(String consentId) {
    this.consentId = consentId;
  }

  public String getCreationDateTime() {
    return creationDateTime;
  }

  public void setCreationDateTime(String creationDateTime) {
    this.creationDateTime = creationDateTime;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getStatusUpdateDateTime() {
    return statusUpdateDateTime;
  }

  public void setStatusUpdateDateTime(String statusUpdateDateTime) {
    this.statusUpdateDateTime = statusUpdateDateTime;
  }

  public String getTransactionIdentification() {
    return transactionIdentification;
  }

  public void setTransactionIdentification(String transactionIdentification) {
    this.transactionIdentification = transactionIdentification;
  }
}
