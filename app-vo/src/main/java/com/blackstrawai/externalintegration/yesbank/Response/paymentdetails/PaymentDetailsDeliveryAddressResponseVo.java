package com.blackstrawai.externalintegration.yesbank.Response.paymentdetails;

import com.blackstrawai.externalintegration.yesbank.Response.common.DeliveryAddressBaseResponseVo;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentDetailsDeliveryAddressResponseVo extends DeliveryAddressBaseResponseVo {

  @JsonProperty("AddressLine")
  private String addressLine;
  @JsonProperty("CountySubDivision")
  private String countySubDivision = null;

  public void setAddressLine(String addressLine) {
    this.addressLine = addressLine;
  }

  public void setCountySubDivision(String countySubDivision) {
    this.countySubDivision = countySubDivision;
  }
}
