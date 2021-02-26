package com.blackstrawai.response.externalintegration.banking.yesbank;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.yesbank.Response.payments.CopyPaymentTransactionVo;

public class CopyPaymentTransactionResponse extends BaseResponse {

  private CopyPaymentTransactionVo data;

  public CopyPaymentTransactionVo getData() {
    return data;
  }

  public void setData(CopyPaymentTransactionVo data) {
    this.data = data;
  }
}
