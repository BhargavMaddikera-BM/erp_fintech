package com.blackstrawai.response.externalintegration.banking.yesbank;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.yesbank.Response.payments.PaymentResponseVo;

public class PaymentTransactionsResponse extends BaseResponse {

  private PaymentResponseVo data;

  public PaymentResponseVo getData() {
    return data;
  }

  public void setData(PaymentResponseVo data) {
    this.data = data;
  }
}
