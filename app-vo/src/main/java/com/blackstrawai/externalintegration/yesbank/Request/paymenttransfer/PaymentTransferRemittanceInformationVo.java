package com.blackstrawai.externalintegration.yesbank.Request.paymenttransfer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentTransferRemittanceInformationVo {

  @JsonProperty("Reference")
  private String reference;

  @JsonProperty("Unstructured")
  private PaymentTransferRemittanceUnstructuredVo unstructured;
  @JsonIgnore private String billReference;
  @JsonIgnore private String paymentReference;

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

  public void setReference(String reference) {
    this.reference = reference;
  }

  public void setUnstructured(PaymentTransferRemittanceUnstructuredVo unstructured) {
    this.unstructured = unstructured;
  }
}
