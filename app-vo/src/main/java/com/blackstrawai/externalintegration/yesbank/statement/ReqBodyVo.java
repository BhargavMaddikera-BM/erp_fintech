package com.blackstrawai.externalintegration.yesbank.statement;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReqBodyVo {
  @JsonProperty("customer_id")
  private String customerId;
  @JsonProperty("cod_acct_no")
  private String accountNo;
  @JsonProperty("txn_start_date")
  private String txnStartDate;
  @JsonProperty("txn_end_date")
  private String txnEndDate;

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public String getAccountNo() {
    return accountNo;
  }

  public void setAccountNo(String accountNo) {
    this.accountNo = accountNo;
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
}
