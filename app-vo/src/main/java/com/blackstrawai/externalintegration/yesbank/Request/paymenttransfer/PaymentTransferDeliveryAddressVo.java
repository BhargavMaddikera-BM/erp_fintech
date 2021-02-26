package com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PaymentTransferDeliveryAddressVo {

  @JsonProperty("AddressLine")
  private List<String> addressLine;
  @JsonProperty("StreetName")
  private String streetName;
  @JsonProperty("BuildingNumber")
  private String buildingNumber;
  @JsonProperty("PostCode")
  private String postCode;
  @JsonProperty("TownName")
  private String townName;
  @JsonProperty("CountySubDivision")
  private List<String> countySubDivision = null;
  @JsonProperty("Country")
  private String country;

  public void setAddressLine(List<String> addressLine) {
    this.addressLine = addressLine;
  }

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

  public void setCountySubDivision(List<String> countySubDivision) {
    this.countySubDivision = countySubDivision;
  }

  public void setCountry(String country) {
    this.country = country;
  }
}
