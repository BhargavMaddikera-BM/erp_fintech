package com.blackstrawai.externalintegration.yesbank.statement;

public class BankStatementInfoVo {

  private String txnDate;
  private String txnDesc;
  private String valueDate;
  private String amtWithdrawal;
  private String amtDeposit;
  private String refChqNum;
  private String balance;

  public String getTxnDate() {
    return txnDate;
  }

  public void setTxnDate(String txnDate) {
    this.txnDate = txnDate;
  }

  public String getTxnDesc() {
    return txnDesc;
  }

  public void setTxnDesc(String txnDesc) {
    this.txnDesc = txnDesc;
  }

  public String getValueDate() {
    return valueDate;
  }

  public void setValueDate(String valueDate) {
    this.valueDate = valueDate;
  }

  public String getAmtWithdrawal() {
    return amtWithdrawal;
  }

  public void setAmtWithdrawal(String amtWithdrawal) {
    this.amtWithdrawal = amtWithdrawal;
  }

  public String getAmtDeposit() {
    return amtDeposit;
  }

  public void setAmtDeposit(String amtDeposit) {
    this.amtDeposit = amtDeposit;
  }

  public String getRefChqNum() {
    return refChqNum;
  }

  public void setRefChqNum(String refChqNum) {
    this.refChqNum = refChqNum;
  }

  public String getBalance() {
    return balance;
  }

  public void setBalance(String balance) {
    this.balance = balance;
  }
}
