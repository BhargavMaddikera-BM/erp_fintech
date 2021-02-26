package com.blackstrawai.externalintegration.yesbank.Response.paymentdetails;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentDetailsRiskResponseVo {

  @JsonProperty("PaymentContextCode")
  private String PaymentContextCode;
  @JsonProperty("DeliveryAddress")
  private PaymentDetailsDeliveryAddressResponseVo deliveryAddress;

  public void setPaymentContextCode(String paymentContextCode) {
    PaymentContextCode = paymentContextCode;
  }

  public void setDeliveryAddress(PaymentDetailsDeliveryAddressResponseVo deliveryAddress) {
    this.deliveryAddress = deliveryAddress;
  }
}
