package com.blackstrawai.response.externalintegration.banking.yesbank;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.yesbank.Response.payments.PaymentTransactionResponseVo;

public class PaymentTransactionResponse extends BaseResponse {

  private PaymentTransactionResponseVo data;

    public PaymentTransactionResponseVo getData() {
        return data;
    }

    public void setData(PaymentTransactionResponseVo data) {
        this.data = data;
    }
}
