package com.blackstrawai.externalintegration.yesbank.Response.payments;

public class PaymentTransactionInfoVo {
  private String transactionId;
  private String transactionDate;
  private String bankReferenceNo;
  private String Status;

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public String getTransactionDate() {
    return transactionDate;
  }

  public void setTransactionDate(String transactionDate) {
    this.transactionDate = transactionDate;
  }

  public String getBankReferenceNo() {
    return bankReferenceNo;
  }

  public void setBankReferenceNo(String bankReferenceNo) {
    this.bankReferenceNo = bankReferenceNo;
  }

  public String getStatus() {
    return Status;
  }

  public void setStatus(String status) {
    Status = status;
  }
}
