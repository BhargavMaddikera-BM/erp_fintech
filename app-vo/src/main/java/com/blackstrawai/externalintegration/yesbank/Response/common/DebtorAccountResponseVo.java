package com.blackstrawai.externalintegration.yesbank.Response.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DebtorAccountResponseVo {

  @JsonProperty("Identification")
  private String identification;
  @JsonProperty("SecondaryIdentification")
  private String secondaryIdentification;

  public void setIdentification(String identification) {
    this.identification = identification;
  }

  public void setSecondaryIdentification(String secondaryIdentification) {
    this.secondaryIdentification = secondaryIdentification;
  }
}
