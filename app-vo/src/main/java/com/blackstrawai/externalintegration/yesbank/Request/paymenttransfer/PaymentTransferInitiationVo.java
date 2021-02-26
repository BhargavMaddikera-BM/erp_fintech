package com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentTransferInitiationVo {

  @JsonProperty("InstructionIdentification")
  private String instructionIdentification;

  @JsonProperty("EndToEndIdentification")
  private String endToEndIdentification;
  @JsonProperty("InstructedAmount")
  private PaymentTransferInstructedAmountVo instructedAmount;
  @JsonProperty("DebtorAccount")
  private PaymentTransferDebtorAccountVo debtorAccount;
  @JsonProperty("CreditorAccount")
  private PaymentTransferCreditorAccountVo creditorAccount;
  @JsonProperty("RemittanceInformation")
  private PaymentTransferRemittanceInformationVo remittanceInformation;
  @JsonProperty("ClearingSystemIdentification")
  private String clearingSystemIdentification;

  public String getInstructionIdentification() {
    return instructionIdentification;
  }

  public void setInstructionIdentification(String instructionIdentification) {
    this.instructionIdentification = instructionIdentification;
  }

  public String getEndToEndIdentification() {
    return endToEndIdentification;
  }

  public void setEndToEndIdentification(String endToEndIdentification) {
    this.endToEndIdentification = endToEndIdentification;
  }

  public PaymentTransferInstructedAmountVo getInstructedAmount() {
    return instructedAmount;
  }

  public void setInstructedAmount(PaymentTransferInstructedAmountVo instructedAmount) {
    this.instructedAmount = instructedAmount;
  }

  public PaymentTransferDebtorAccountVo getDebtorAccount() {
    return debtorAccount;
  }

  public void setDebtorAccount(PaymentTransferDebtorAccountVo debtorAccount) {
    this.debtorAccount = debtorAccount;
  }

  public PaymentTransferCreditorAccountVo getCreditorAccount() {
    return creditorAccount;
  }

  public void setCreditorAccount(PaymentTransferCreditorAccountVo creditorAccount) {
    this.creditorAccount = creditorAccount;
  }

  public PaymentTransferRemittanceInformationVo getRemittanceInformation() {
    return remittanceInformation;
  }

  public void setRemittanceInformation(
      PaymentTransferRemittanceInformationVo remittanceInformation) {
    this.remittanceInformation = remittanceInformation;
  }

  public String getClearingSystemIdentification() {
    return clearingSystemIdentification;
  }

  public void setClearingSystemIdentification(String clearingSystemIdentification) {
    this.clearingSystemIdentification = clearingSystemIdentification;
  }
}
