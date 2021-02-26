package com.blackstrawai.response.externalintegration.banking.yesbank;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.yesbank.Response.common.AccountVo;

public class AccountOverviewResponse extends BaseResponse {

  private AccountVo data;

  public AccountVo getData() {
    return data;
  }

  public void setData(AccountVo data) {
    this.data = data;
  }
}
