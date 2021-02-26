package com.blackstrawai.externalintegration.yesbank.Response.payments;

public class PaymentTransactionResponseVo {

  private PaymentTransactionInfoVo paymentTransactions;
  private PaymentInfoVo paymentInformation;
  private PaymentAccountingInfoVo paymentAccountingInformation;

  public PaymentTransactionInfoVo getPaymentTransactions() {
    return paymentTransactions;
  }

  public void setPaymentTransactions(PaymentTransactionInfoVo paymentTransactions) {
    this.paymentTransactions = paymentTransactions;
  }

  public PaymentInfoVo getPaymentInformation() {
    return paymentInformation;
  }

  public void setPaymentInformation(PaymentInfoVo paymentInformation) {
    this.paymentInformation = paymentInformation;
  }

  public PaymentAccountingInfoVo getPaymentAccountingInformation() {
    return paymentAccountingInformation;
  }

  public void setPaymentAccountingInformation(
      PaymentAccountingInfoVo paymentAccountingInformation) {
    this.paymentAccountingInformation = paymentAccountingInformation;
  }
}
