package com.blackstrawai.externalintegration.yesbank.Response.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LinksVo {

  @JsonProperty("Self")
  private String self;

  public void setSelf(String self) {
    this.self = self;
  }
}
