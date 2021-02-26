package com.blackstrawai.externalintegration.yesbank.Response.paymenttransfer;

import com.blackstrawai.externalintegration.yesbank.Response.common.CreditorAccountResponseVo;
import com.blackstrawai.externalintegration.yesbank.Response.common.InitiationBaseResponseVo;
import com.blackstrawai.externalintegration.yesbank.Response.common.RemittanceInformationResponseVo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentTransferInitiationResponseVo extends InitiationBaseResponseVo {

  @JsonProperty("CreditorAccount")
  private CreditorAccountResponseVo creditorAccount;
  @JsonProperty("RemittanceInformation")
  private RemittanceInformationResponseVo remittanceInformationResponseVo;
  @JsonProperty("ClearingSystemIdentification")
  private String clearingSystemIdentification;

  public CreditorAccountResponseVo getCreditorAccount() {
    return creditorAccount;
  }

  public void setCreditorAccount(CreditorAccountResponseVo creditorAccount) {
    this.creditorAccount = creditorAccount;
  }

  public RemittanceInformationResponseVo getRemittanceInformationResponseVo() {
    return remittanceInformationResponseVo;
  }

  public void setRemittanceInformationResponseVo(
      RemittanceInformationResponseVo remittanceInformationResponseVo) {
    this.remittanceInformationResponseVo = remittanceInformationResponseVo;
  }

  public String getClearingSystemIdentification() {
    return clearingSystemIdentification;
  }

  public void setClearingSystemIdentification(String clearingSystemIdentification) {
    this.clearingSystemIdentification = clearingSystemIdentification;
  }
}
