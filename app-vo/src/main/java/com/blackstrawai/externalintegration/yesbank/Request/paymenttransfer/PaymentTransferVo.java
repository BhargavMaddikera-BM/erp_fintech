package com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentTransferVo {

  @JsonProperty("Data")
  private PaymentTransferDomesticPaymentsVo data;
  @JsonProperty("Risk")
  private PaymentTransferRiskVo risk;

  public PaymentTransferDomesticPaymentsVo getData() {
    return data;
  }

  public void setData(PaymentTransferDomesticPaymentsVo data) {
    this.data = data;
  }

  public PaymentTransferRiskVo getRisk() {
    return risk;
  }

  public void setRisk(PaymentTransferRiskVo risk) {
    this.risk = risk;
  }
}
