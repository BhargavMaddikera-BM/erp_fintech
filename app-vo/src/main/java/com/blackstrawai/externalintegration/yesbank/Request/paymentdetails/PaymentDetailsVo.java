package com.blackstrawai.externalintegration.yesbank.Request.paymentdetails;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentDetailsVo {

  @JsonProperty("ConsentId")
  private String consentId;

  @JsonProperty("InstrId")
  private String InstrId;

  @JsonProperty("SecondaryIdentification")
  private String secondaryIdentification;

  public void setConsentId(String consentId) {
    this.consentId = consentId;
  }

  public void setInstrId(String instrId) {
    InstrId = instrId;
  }

  public void setSecondaryIdentification(String secondaryIdentification) {
    this.secondaryIdentification = secondaryIdentification;
  }
}
