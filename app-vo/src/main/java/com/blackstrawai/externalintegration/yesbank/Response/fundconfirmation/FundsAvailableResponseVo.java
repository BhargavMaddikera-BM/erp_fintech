package com.blackstrawai.externalintegration.yesbank.Response.fundconfirmation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FundsAvailableResponseVo {

  @JsonProperty("FundsAvailableDateTime")
  private String fundsAvailableDateTime;
  @JsonProperty("FundsAvailable")
  private String FundsAvailable;
  @JsonProperty("BalanceAmount")
  private String balanceAmount;

  public String getFundsAvailableDateTime() {
    return fundsAvailableDateTime;
  }

  public void setFundsAvailableDateTime(String fundsAvailableDateTime) {
    this.fundsAvailableDateTime = fundsAvailableDateTime;
  }

  public String getFundsAvailable() {
    return FundsAvailable;
  }

  public void setFundsAvailable(String fundsAvailable) {
    FundsAvailable = fundsAvailable;
  }

  public String getBalanceAmount() {
    return balanceAmount;
  }

  public void setBalanceAmount(String balanceAmount) {
    this.balanceAmount = balanceAmount;
  }
}
