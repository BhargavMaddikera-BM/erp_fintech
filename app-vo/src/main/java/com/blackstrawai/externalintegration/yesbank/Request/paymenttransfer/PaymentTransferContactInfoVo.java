package com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentTransferContactInfoVo {

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
