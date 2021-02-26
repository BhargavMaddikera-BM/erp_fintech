package com.blackstrawai.externalintegration.yesbank.statement;

import java.util.List;

public class BankStatementDetailsResponseVo {

  private int customerId;
  private String accountNumber;
  private List<BankStatementInfoVo> statement;

  public List<BankStatementInfoVo> getStatement() {
    return statement;
  }

  public void setStatement(List<BankStatementInfoVo> statement) {
    this.statement = statement;
  }

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }
}
