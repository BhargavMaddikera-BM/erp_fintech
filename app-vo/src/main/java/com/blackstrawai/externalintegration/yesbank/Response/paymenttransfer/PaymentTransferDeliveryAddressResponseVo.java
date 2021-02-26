package com.blackstrawai.externalintegration.yesbank.Response.paymenttransfer;

import com.blackstrawai.externalintegration.yesbank.Response.common.DeliveryAddressBaseResponseVo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentTransferDeliveryAddressResponseVo extends DeliveryAddressBaseResponseVo {

  @JsonProperty("AddressLine")
  private List<String> addressLine;
  @JsonProperty("CountySubDivision")
  private List<String> countySubDivision = null;

  public void setAddressLine(List<String> addressLine) {
    this.addressLine = addressLine;
  }

  public void setCountySubDivision(List<String> countySubDivision) {
    this.countySubDivision = countySubDivision;
  }
}
