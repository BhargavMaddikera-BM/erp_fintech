package com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentTransferDomesticPaymentsVo {

  @JsonProperty("ConsentId")
  private String consentId;
  @JsonProperty("Initiation")
  private PaymentTransferInitiationVo initiation;

  public String getConsentId() {
    return consentId;
  }

  public void setConsentId(String consentId) {
    this.consentId = consentId;
  }

  public PaymentTransferInitiationVo getInitiation() {
    return initiation;
  }

  public void setInitiation(PaymentTransferInitiationVo initiation) {
    this.initiation = initiation;
  }
}
