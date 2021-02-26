package com.blackstrawai.externalintegration.yesbank.Response.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactInformationUnstructuredResponseVo {

  @JsonProperty("ContactInformation")
  private ContactInformationResponseVo contactInformation;

  public void setContactInformation(ContactInformationResponseVo contactInformation) {
    this.contactInformation = contactInformation;
  }
}
