package com.blackstrawai.externalintegration.yesbank.Response.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InitiationBaseResponseVo {

  @JsonProperty("InstructionIdentification")
  private String instructionIdentification;
  @JsonProperty("EndToEndIdentification")
  private String endToEndIdentification;
  @JsonProperty("InstructedAmount")
  private InstructedAmountResponseVo instructedAmount;
  @JsonProperty("DebtorAccount")
  private DebtorAccountResponseVo debtorAccount;

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

  public InstructedAmountResponseVo getInstructedAmount() {
    return instructedAmount;
  }

  public void setInstructedAmount(InstructedAmountResponseVo instructedAmount) {
    this.instructedAmount = instructedAmount;
  }

  public DebtorAccountResponseVo getDebtorAccount() {
    return debtorAccount;
  }

  public void setDebtorAccount(DebtorAccountResponseVo debtorAccount) {
    this.debtorAccount = debtorAccount;
  }
}
