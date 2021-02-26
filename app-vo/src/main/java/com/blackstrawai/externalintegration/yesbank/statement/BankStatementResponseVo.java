package com.blackstrawai.externalintegration.yesbank.statement;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BankStatementResponseVo {
  @JsonProperty("AdhocStatementRes")
  private AdhocStatementResVo adhocStatementRes;

  public AdhocStatementResVo getAdhocStatementRes() {
    return adhocStatementRes;
  }

  public void setAdhocStatementRes(AdhocStatementResVo adhocStatementRes) {
    this.adhocStatementRes = adhocStatementRes;
  }
}
