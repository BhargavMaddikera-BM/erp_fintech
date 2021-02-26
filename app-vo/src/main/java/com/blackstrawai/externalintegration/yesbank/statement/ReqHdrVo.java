package com.blackstrawai.externalintegration.yesbank.statement;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReqHdrVo {
  @JsonProperty("ConsumerContext")
  private ConsumerContextVo consumerContext;

  @JsonProperty("ServiceContext")
  private ServiceContextVo serviceContext;

  public ConsumerContextVo getConsumerContext() {
    return consumerContext;
  }

  public void setConsumerContext(ConsumerContextVo consumerContextVo) {
    this.consumerContext = consumerContextVo;
  }

  public ServiceContextVo getServiceContext() {
    return serviceContext;
  }

  public void setServiceContext(ServiceContextVo serviceContextVo) {
    this.serviceContext = serviceContextVo;
  }
}
