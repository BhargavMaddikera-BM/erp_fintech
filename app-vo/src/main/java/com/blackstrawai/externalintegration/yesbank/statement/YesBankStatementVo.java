package com.blackstrawai.externalintegration.yesbank.statement;

import com.fasterxml.jackson.annotation.JsonProperty;

public class YesBankStatementVo {
  @JsonProperty("AdhocStatementReq")
  private AdhocStatementReqVo adhocStatementReq;

  public AdhocStatementReqVo getAdhocStatementReq() {
    return adhocStatementReq;
  }

  public void setAdhocStatementReq(AdhocStatementReqVo adhocStatementReq) {
    this.adhocStatementReq = adhocStatementReq;
  }
}
