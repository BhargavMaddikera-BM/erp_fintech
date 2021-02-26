package com.blackstrawai.externalintegration.yesbank.Response.paymenttransfer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentTransferRiskResponseVo {

  @JsonIgnore
  @JsonProperty("DeliveryAddress")
  private PaymentTransferDeliveryAddressResponseVo deliveryAddress;

  public void setDeliveryAddress(PaymentTransferDeliveryAddressResponseVo deliveryAddress) {
    this.deliveryAddress = deliveryAddress;
  }
}
