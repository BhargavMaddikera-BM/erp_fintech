package com.blackstrawai.externalintegration.yesbank.Response.paymenttransfer;

import com.blackstrawai.externalintegration.yesbank.Response.common.CodeVo;
import com.blackstrawai.externalintegration.yesbank.Response.common.MetaData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentTransferResponseVo extends MetaData {

  @JsonProperty("Data")
  private PaymentTransferDomesticPaymentsResponseVo data;

  @JsonProperty("Risk")
  private PaymentTransferRiskResponseVo risk;

  @JsonProperty("Code")
  private CodeVo code;

  public void setRisk(PaymentTransferRiskResponseVo risk) {
    this.risk = risk;
  }

  public PaymentTransferDomesticPaymentsResponseVo getData() {
    return data;
  }

  public void setData(PaymentTransferDomesticPaymentsResponseVo data) {
    this.data = data;
  }

  public CodeVo getCode() {
    return code;
  }

  public void setCode(CodeVo code) {
    this.code = code;
  }
}
