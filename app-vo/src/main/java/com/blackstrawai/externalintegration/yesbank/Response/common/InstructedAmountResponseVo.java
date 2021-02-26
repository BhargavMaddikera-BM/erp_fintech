package com.blackstrawai.externalintegration.yesbank.Response.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InstructedAmountResponseVo {

  @JsonProperty("Amount")
  private String amount;
  @JsonProperty("Currency")
  private String currency;

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }
}
