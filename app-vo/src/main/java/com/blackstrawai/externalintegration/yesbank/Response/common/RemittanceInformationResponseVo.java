package com.blackstrawai.externalintegration.yesbank.Response.common;

import com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer.PaymentTransferRemittanceUnstructuredVo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RemittanceInformationResponseVo {

  @JsonProperty("Reference")
  private String reference;
  @JsonProperty("Unstructured")
  private PaymentTransferRemittanceUnstructuredVo unstructured;

  public void setReference(String reference) {
    this.reference = reference;
  }

  public void setUnstructured(PaymentTransferRemittanceUnstructuredVo unstructured) {
    this.unstructured = unstructured;
  }
}
