package com.blackstrawai.externalintegration.yesbank.dropdowns;

import java.util.List;

public class PaymentBillReferenceVo {

  private List<String> billReference;
  private List<String> paymentReference;

  public List<String> getBillReference() {
    return billReference;
  }

  public void setBillReference(List<String> billReference) {
    this.billReference = billReference;
  }

  public List<String> getPaymentReference() {
    return paymentReference;
  }

  public void setPaymentReference(List<String> paymentReference) {
    this.paymentReference = paymentReference;
  }
}
