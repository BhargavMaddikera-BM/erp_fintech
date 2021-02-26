package com.blackstrawai.request.externalintegration.banking.yesbank;

public class DebtorAccountRequest {

  private String customerId;
  private String accountNumber;

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }
}
