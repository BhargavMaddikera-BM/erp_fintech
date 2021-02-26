package com.blackstrawai.response.externalintegration.messaging;

import com.blackstrawai.common.BaseResponse;

public class MessagingResponse extends BaseResponse {

  private String data;
  
  private String mobileNo;

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

public String getMobileNo() {
	return mobileNo;
}

public void setMobileNo(String mobileNo) {
	this.mobileNo = mobileNo;
}
  
}
