package com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentTransferContactInfoUnstructuredVo {

  @JsonProperty("ContactInformation")
  private PaymentTransferContactInfoVo contactInformation;

  public void setContactInformation(PaymentTransferContactInfoVo contactInformation) {
    this.contactInformation = contactInformation;
  }
}
