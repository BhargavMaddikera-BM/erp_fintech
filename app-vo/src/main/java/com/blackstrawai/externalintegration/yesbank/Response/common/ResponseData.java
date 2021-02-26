package com.blackstrawai.externalintegration.yesbank.Response.common;

public class ResponseData<T> extends MetaData {

  private T data;
  private T risk;

  public void setData(T data) {
    this.data = data;
  }

  public void setRisk(T risk) {
    this.risk = risk;
  }
}
