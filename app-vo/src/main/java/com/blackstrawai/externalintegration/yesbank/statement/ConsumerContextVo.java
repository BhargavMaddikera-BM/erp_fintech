package com.blackstrawai.externalintegration.yesbank.statement;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConsumerContextVo {
  @JsonProperty("RequesterID")
  private String requesterID;

  public String getRequesterID() {
    return requesterID;
  }

  public void setRequesterID(String requesterID) {
    this.requesterID = requesterID;
  }
}
