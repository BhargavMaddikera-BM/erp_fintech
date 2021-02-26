package com.blackstrawai.externalintegration.yesbank.Request.fundconfirmation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AvailableBalanceDebtorAccountVo {

  @JsonProperty("ConsentId")
  private String consentId;

  @JsonProperty("Identification")
  private String identification;

  @JsonProperty("SecondaryIdentification")
  private String secondaryIdentification;

  public void setConsentId(String consentId) {
    this.consentId = consentId;
  }

  public void setIdentification(String identification) {
    this.identification = identification;
  }

  public void setSecondaryIdentification(String secondaryIdentification) {
    this.secondaryIdentification = secondaryIdentification;
  }
}
