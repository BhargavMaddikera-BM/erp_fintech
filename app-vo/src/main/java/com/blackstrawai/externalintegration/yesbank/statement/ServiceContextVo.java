package com.blackstrawai.externalintegration.yesbank.statement;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceContextVo {

  @JsonProperty("ServiceName")
  private String serviceName;
  @JsonProperty("ReqRefNum")
  private String reqRefNum;
  @JsonProperty("ReqRefTimeStamp")
  private String reqRefTimeStamp;
  @JsonProperty("ServiceVersionNo")
  private String serviceVersionNo;

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public String getReqRefNum() {
    return reqRefNum;
  }

  public void setReqRefNum(String reqRefNum) {
    this.reqRefNum = reqRefNum;
  }

  public String getReqRefTimeStamp() {
    return reqRefTimeStamp;
  }

  public void setReqRefTimeStamp(String reqRefTimeStamp) {
    this.reqRefTimeStamp = reqRefTimeStamp;
  }

  public String getServiceVersionNo() {
    return serviceVersionNo;
  }

  public void setServiceVersionNo(String serviceVersionNo) {
    this.serviceVersionNo = serviceVersionNo;
  }
}
