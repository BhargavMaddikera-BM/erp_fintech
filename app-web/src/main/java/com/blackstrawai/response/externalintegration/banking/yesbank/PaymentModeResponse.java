package com.blackstrawai.response.externalintegration.banking.yesbank;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.yesbank.dropdowns.PaymentsDropDownVo;

public class PaymentModeResponse extends BaseResponse {
  private PaymentsDropDownVo data;

  public PaymentsDropDownVo getData() {
    return data;
  }

  public void setData(PaymentsDropDownVo data) {
    this.data = data;
  }
}
