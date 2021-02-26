package com.blackstrawai.externalintegration.yesbank.statement;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ResBodyVo {
  @JsonProperty("customer_id")
  private int customerId;
  @JsonProperty("cod_acct_no")
  private String accountNumber;
  @JsonProperty("cust_name")
  private String custName;
  @JsonProperty("txn_start_date")
  private String txnStartDate;
  @JsonProperty("txn_end_date")
  private String txnEndDate;
  @JsonProperty("opening_balance")
  private String openingBalance;
  @JsonProperty("closing_balance")
  private String closingBalance;
  @JsonProperty("debit_count")
  private int debitCount;
  @JsonProperty("credit_count")
  private int creditCount;
  @JsonProperty("error_code")
  private int errorCode;
  @JsonProperty("error_msg")
  private String errorMsg;
  @JsonProperty("error_reason")
  private String errorReason;
  @JsonProperty("statement")
  private List<StatementVo> statement;

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public int getDebitCount() {
    return debitCount;
  }

  public void setDebitCount(int debitCount) {
    this.debitCount = debitCount;
  }

  public int getCreditCount() {
    return creditCount;
  }

  public void setCreditCount(int creditCount) {
    this.creditCount = creditCount;
  }

  public int getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public String getCustName() {
    return custName;
  }

  public void setCustName(String custName) {
    this.custName = custName;
  }

  public String getTxnStartDate() {
    return txnStartDate;
  }

  public void setTxnStartDate(String txnStartDate) {
    this.txnStartDate = txnStartDate;
  }

  public String getTxnEndDate() {
    return txnEndDate;
  }

  public void setTxnEndDate(String txnEndDate) {
    this.txnEndDate = txnEndDate;
  }

  public String getOpeningBalance() {
    return openingBalance;
  }

  public void setOpeningBalance(String openingBalance) {
    this.openingBalance = openingBalance;
  }

  public String getClosingBalance() {
    return closingBalance;
  }

  public void setClosingBalance(String closingBalance) {
    this.closingBalance = closingBalance;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public String getErrorReason() {
    return errorReason;
  }

  public void setErrorReason(String errorReason) {
    this.errorReason = errorReason;
  }

  public List<StatementVo> getStatement() {
    return statement;
  }

  public void setStatement(List<StatementVo> statement) {
    this.statement = statement;
  }
}
