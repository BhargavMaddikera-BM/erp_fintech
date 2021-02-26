package com.blackstrawai.externalintegration.yesbank.Response.paymentdetails;

import com.blackstrawai.externalintegration.yesbank.Response.common.MetaData;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentDetailsResponseVo extends MetaData {

  @JsonProperty("Data")
  private PaymentDetailsDomesticResponseVo data;
  @JsonProperty("Risk")
  private PaymentDetailsRiskResponseVo risk;

  public void setData(PaymentDetailsDomesticResponseVo data) {
    this.data = data;
  }

  public void setRisk(PaymentDetailsRiskResponseVo risk) {
    this.risk = risk;
  }
}
