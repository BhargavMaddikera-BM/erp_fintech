package com.blackstrawai.externalintegration.yesbank.Response.payments;

public class PaymentAccountingInfoVo {
  private String billReference;
  private String paymentReference;

  public String getBillReference() {
    return billReference;
  }

  public void setBillReference(String billReference) {
    this.billReference = billReference;
  }

  public String getPaymentReference() {
    return paymentReference;
  }

  public void setPaymentReference(String paymentReference) {
    this.paymentReference = paymentReference;
  }
}
