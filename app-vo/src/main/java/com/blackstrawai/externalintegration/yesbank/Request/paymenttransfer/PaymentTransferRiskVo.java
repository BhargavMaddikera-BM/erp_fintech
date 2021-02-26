package com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentTransferRiskVo {

  @JsonProperty("DeliveryAddress")
  private PaymentTransferDeliveryAddressVo deliveryAddress;

  public void setDeliveryAddress(PaymentTransferDeliveryAddressVo deliveryAddress) {
    this.deliveryAddress = deliveryAddress;
  }
}
