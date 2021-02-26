package com.blackstrawai.externalintegration.yesbank.statement;

import com.blackstrawai.externalintegration.yesbank.statement.ResBodyVo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdhocStatementResVo {

  @JsonProperty("ResBody")
  private ResBodyVo resBody;

  public ResBodyVo getResBody() {
    return resBody;
  }

  public void setResBody(ResBodyVo resBody) {
    this.resBody = resBody;
  }
}
