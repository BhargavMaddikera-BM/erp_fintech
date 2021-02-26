package com.blackstrawai.externalintegration.yesbank.Response.paymentdetails;

import com.blackstrawai.externalintegration.yesbank.Response.common.TransactionStatusResponseVo;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentDetailsDomesticResponseVo extends TransactionStatusResponseVo {

  @JsonProperty("Initiation")
  private PaymentDetailsInitiationResponseVo initiation;

  public void setInitiation(PaymentDetailsInitiationResponseVo initiation) {
    this.initiation = initiation;
  }
}
