package com.blackstrawai.externalintegration.yesbank.statement;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AdhocStatementReqVo {
  @JsonProperty("ReqHdr")
  private ReqHdrVo reqHdr;
  @JsonProperty("ReqBody")
  private ReqBodyVo reqBody;

  public ReqHdrVo getReqHdr() {
    return reqHdr;
  }

  public void setReqHdr(ReqHdrVo reqHdrVo) {
    this.reqHdr = reqHdrVo;
  }

  public ReqBodyVo getReqBody() {
    return reqBody;
  }

  public void setReqBody(ReqBodyVo reqBodyVo) {
    this.reqBody = reqBodyVo;
  }
}
