package com.blackstrawai.externalintegration.yesbank.Request.fundconfirmation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AvailableBalanceDataVo {

  @JsonProperty("DebtorAccount")
  private AvailableBalanceDebtorAccountVo debtorAccountVo;

  public void setDebtorAccountVo(AvailableBalanceDebtorAccountVo debtorAccountVo) {
    this.debtorAccountVo = debtorAccountVo;
  }
}
