package com.blackstrawai.externalintegration.yesbank.Response.paymentdetails;

import com.blackstrawai.externalintegration.yesbank.Response.common.CreditorAccountResponseVo;
import com.blackstrawai.externalintegration.yesbank.Response.common.RemittanceInformationResponseVo;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentDetailsCreditorAccountResponseVo extends CreditorAccountResponseVo {

  @JsonProperty("RemittanceInformation")
  private RemittanceInformationResponseVo remittanceInformationResponseVo;
  @JsonProperty("ClearingSystemIdentification")
  private String clearingSystemIdentification;

  public void setRemittanceInformationResponseVo(
      RemittanceInformationResponseVo remittanceInformationResponseVo) {
    this.remittanceInformationResponseVo = remittanceInformationResponseVo;
  }

  public void setClearingSystemIdentification(String clearingSystemIdentification) {
    this.clearingSystemIdentification = clearingSystemIdentification;
  }
}
