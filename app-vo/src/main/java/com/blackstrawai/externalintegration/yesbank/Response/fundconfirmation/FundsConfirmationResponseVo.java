package com.blackstrawai.externalintegration.yesbank.Response.fundconfirmation;

import com.blackstrawai.externalintegration.yesbank.Response.common.MetaData;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FundsConfirmationResponseVo extends MetaData {

  @JsonProperty("Data")
  private FundConfirmationResponseDataVo data;

  public FundConfirmationResponseDataVo getData() {
    return data;
  }

  public void setData(FundConfirmationResponseDataVo data) {
    this.data = data;
  }
}
