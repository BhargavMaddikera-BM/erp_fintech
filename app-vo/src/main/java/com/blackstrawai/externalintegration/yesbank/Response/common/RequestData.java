package com.blackstrawai.externalintegration.yesbank.Response.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestData<T> {

  @JsonProperty("Data")
  private T data;

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
