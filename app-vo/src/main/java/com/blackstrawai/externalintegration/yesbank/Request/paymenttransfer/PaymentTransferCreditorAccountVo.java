package com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentTransferCreditorAccountVo {

  @JsonProperty("SchemeName")
  private String schemeName;

  @JsonProperty("Identification")
  private String identification;

  @JsonProperty("Name")
  private String name;

  @JsonProperty("Unstructured")
  private PaymentTransferContactInfoUnstructuredVo unstructured;

  @JsonIgnore private String beneficiaryId;
  @JsonIgnore private String beneficiaryType;

  public void setSchemeName(String schemeName) {
    this.schemeName = schemeName;
  }

  public void setIdentification(String identification) {
    this.identification = identification;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setUnstructured(PaymentTransferContactInfoUnstructuredVo unstructured) {
    this.unstructured = unstructured;
  }

  public String getBeneficiaryId() {
    return beneficiaryId;
  }

  public void setBeneficiaryId(String beneficiaryId) {
    this.beneficiaryId = beneficiaryId;
  }

  public String getBeneficiaryType() {
    return beneficiaryType;
  }

  public void setBeneficiaryType(String beneficiaryType) {
    this.beneficiaryType = beneficiaryType;
  }
}
