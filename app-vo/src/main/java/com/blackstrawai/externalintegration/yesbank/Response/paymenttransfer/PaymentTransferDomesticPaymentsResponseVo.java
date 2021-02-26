package com.blackstrawai.externalintegration.yesbank.Response.paymenttransfer;

import com.blackstrawai.externalintegration.yesbank.Response.common.TransactionStatusResponseVo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentTransferDomesticPaymentsResponseVo extends TransactionStatusResponseVo {

  @JsonProperty("Initiation")
  private PaymentTransferInitiationResponseVo initiation;

  public PaymentTransferInitiationResponseVo getInitiation() {
    return initiation;
  }

  public void setInitiation(PaymentTransferInitiationResponseVo initiation) {
    this.initiation = initiation;
  }
}
