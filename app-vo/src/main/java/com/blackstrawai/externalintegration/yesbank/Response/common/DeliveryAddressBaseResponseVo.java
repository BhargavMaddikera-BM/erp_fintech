package com.blackstrawai.externalintegration.yesbank.Response.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryAddressBaseResponseVo {

  @JsonProperty("StreetName")
  private String streetName;
  @JsonProperty("BuildingNumber")
  private String buildingNumber;
  @JsonProperty("PostCode")
  private String postCode;
  @JsonProperty("TownName")
  private String townName;
  @JsonProperty("Country")
  private String country;

  public void setStreetName(String streetName) {
    this.streetName = streetName;
  }

  public void setBuildingNumber(String buildingNumber) {
    this.buildingNumber = buildingNumber;
  }

  public void setPostCode(String postCode) {
    this.postCode = postCode;
  }

  public void setTownName(String townName) {
    this.townName = townName;
  }

  public void setCountry(String country) {
    this.country = country;
  }
}
