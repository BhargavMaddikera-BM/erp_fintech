package com.blackstrawai.externalintegration.yesbank.statement;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatementVo {

  @JsonProperty("txn_date")
  private String txnDate;

  @JsonProperty("txn_desc")
  private String txnDesc;

  @JsonProperty("post_date")
  private String valueDate;

  @JsonProperty("amt_withdrawal")
  private String amtWithdrawal;

  @JsonProperty("amt_deposit")
  private String amtDeposit;

  @JsonProperty("ref_chq_num")
  private String refChqNum;

  @JsonProperty("balance")
  private String balance;

  @JsonProperty("cod_txn_literal")
  private String codTxnLiteral;

  @JsonProperty("ref_usr_no")
  private String refUsrNo;

  public String getCodTxnLiteral() {
    return codTxnLiteral;
  }

  public void setCodTxnLiteral(String codTxnLiteral) {
    this.codTxnLiteral = codTxnLiteral;
  }

  public String getRefUsrNo() {
    return refUsrNo;
  }

  public void setRefUsrNo(String refUsrNo) {
    this.refUsrNo = refUsrNo;
  }

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
