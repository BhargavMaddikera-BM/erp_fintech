package com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentTransferRemittanceUnstructuredVo {

  @JsonProperty("CreditorReferenceInformation")
  private String creditorReferenceInformation;

  public void setCreditorReferenceInformation(String creditorReferenceInformation) {
    this.creditorReferenceInformation = creditorReferenceInformation;
  }
}
