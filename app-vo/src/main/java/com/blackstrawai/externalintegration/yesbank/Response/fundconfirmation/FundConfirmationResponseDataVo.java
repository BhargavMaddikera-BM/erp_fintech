package com.blackstrawai.externalintegration.yesbank.Response.fundconfirmation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class FundConfirmationResponseDataVo {

  @JsonProperty("FundsAvailableResult")
  private FundsAvailableResponseVo FundsAvailableVo;

  public FundsAvailableResponseVo getFundsAvailableVo() {
    return FundsAvailableVo;
  }

  public void setFundsAvailableVo(FundsAvailableResponseVo fundsAvailableVo) {
    FundsAvailableVo = fundsAvailableVo;
  }
}
