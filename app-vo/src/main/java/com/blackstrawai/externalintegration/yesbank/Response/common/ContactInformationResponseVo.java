package com.blackstrawai.externalintegration.yesbank.Response.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ContactInformationResponseVo {

  @JsonProperty("EmailAddress")
  private String emailAddress;
  @JsonProperty("MobileNumber")
  private String mobileNumber;

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }
}
