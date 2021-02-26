package com.blackstrawai.response.externalintegration.banking.yesbank;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.yesbank.Response.fundconfirmation.FundsAvailableResponseVo;

public class FundsAvailableResponse extends BaseResponse {

  private FundsAvailableResponseVo data;

  public FundsAvailableResponseVo getData() {
    return data;
  }

  public void setData(FundsAvailableResponseVo data) {
    this.data = data;
  }
}
