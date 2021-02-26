package com.blackstrawai.externalintegration.yesbank.Response.paymentdetails;

import com.blackstrawai.externalintegration.yesbank.Response.common.InitiationBaseResponseVo;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentDetailsInitiationResponseVo extends InitiationBaseResponseVo {

  @JsonProperty("CreditorAccount")
  private PaymentDetailsCreditorAccountResponseVo creditorAccount;

  public void setCreditorAccount(PaymentDetailsCreditorAccountResponseVo creditorAccount) {
    this.creditorAccount = creditorAccount;
  }
}
