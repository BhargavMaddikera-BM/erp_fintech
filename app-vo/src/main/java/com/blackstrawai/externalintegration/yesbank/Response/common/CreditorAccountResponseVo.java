package com.blackstrawai.externalintegration.yesbank.Response.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreditorAccountResponseVo {

  @JsonProperty("SchemeName")
  private String schemeName;
  @JsonProperty("Identification")
  private String identification;
  @JsonProperty("Name")
  private String name;
  @JsonProperty("BeneficiaryCode")
  private String beneficiaryCode;
  @JsonProperty("Unstructured")
  private ContactInformationUnstructuredResponseVo unstructured;

  public String getSchemeName() {
    return schemeName;
  }

  public void setSchemeName(String schemeName) {
    this.schemeName = schemeName;
  }

  public void setIdentification(String identification) {
    this.identification = identification;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setBeneficiaryCode(String beneficiaryCode) {
    this.beneficiaryCode = beneficiaryCode;
  }

  public void setUnstructured(ContactInformationUnstructuredResponseVo unstructured) {
    this.unstructured = unstructured;
  }
}
