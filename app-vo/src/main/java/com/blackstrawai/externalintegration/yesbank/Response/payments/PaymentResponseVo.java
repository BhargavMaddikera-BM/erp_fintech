package com.blackstrawai.externalintegration.yesbank.Response.payments;



import com.blackstrawai.externalintegration.yesbank.beneficiary.BeneficiaryTransactionResponseVo;

import java.util.List;

public class PaymentResponseVo {

  private List<BeneficiaryTransactionResponseVo> paymentTransaction;

  public List<BeneficiaryTransactionResponseVo> getPaymentTransaction() {
    return paymentTransaction;
  }

  public void setPaymentTransaction(List<BeneficiaryTransactionResponseVo> paymentTransaction) {
    this.paymentTransaction = paymentTransaction;
  }
}
