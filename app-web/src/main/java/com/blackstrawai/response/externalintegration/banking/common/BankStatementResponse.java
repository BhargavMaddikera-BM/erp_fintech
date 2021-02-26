package com.blackstrawai.response.externalintegration.banking.common;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.yesbank.statement.BankStatementDetailsResponseVo;

public class BankStatementResponse extends BaseResponse {
  private BankStatementDetailsResponseVo data;

  public BankStatementDetailsResponseVo getData() {
    return data;
  }

  public void setData(BankStatementDetailsResponseVo data) {
    this.data = data;
  }
}

