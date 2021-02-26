package com.blackstrawai.externalintegration.yesbank.Response.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MetaVo {

  @JsonProperty("ErrorCode")
  private String errorCode;
  @JsonProperty("ErrorSeverity")
  private String errorSeverity;
  @JsonProperty("ActionCode")
  private String actionCode;
  @JsonProperty("ActionDescription")
  private String actionDescription;

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorSeverity() {
    return errorSeverity;
  }

  public void setErrorSeverity(String errorSeverity) {
    this.errorSeverity = errorSeverity;
  }

  public String getActionCode() {
    return actionCode;
  }

  public void setActionCode(String actionCode) {
    this.actionCode = actionCode;
  }

  public String getActionDescription() {
    return actionDescription;
  }

  public void setActionDescription(String actionDescription) {
    this.actionDescription = actionDescription;
  }
}
