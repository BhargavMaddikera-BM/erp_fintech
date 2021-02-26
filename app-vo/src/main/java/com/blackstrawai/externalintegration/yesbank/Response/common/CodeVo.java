package com.blackstrawai.externalintegration.yesbank.Response.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CodeVo {

  @JsonProperty("httpCode")
  private String httpCode;

  @JsonProperty("httpMessage")
  private String httpMessage;

  @JsonProperty("Code")
  private String code;

  @JsonProperty("Id")
  private String id;

  @JsonProperty("Message")
  private String message;
  @JsonProperty("ActionCode")
  private String actionCode;
  @JsonProperty("ActionDescription")
  private String ActionDescription;

  public String getActionCode() {
    return actionCode;
  }

  public void setActionCode(String actionCode) {
    this.actionCode = actionCode;
  }

  public String getActionDescription() {
    return ActionDescription;
  }

  public void setActionDescription(String actionDescription) {
    ActionDescription = actionDescription;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getHttpCode() {
    return httpCode;
  }

  public void setHttpCode(String httpCode) {
    this.httpCode = httpCode;
  }

  public String getHttpMessage() {
    return httpMessage;
  }

  public void setHttpMessage(String httpMessage) {
    this.httpMessage = httpMessage;
  }
}
