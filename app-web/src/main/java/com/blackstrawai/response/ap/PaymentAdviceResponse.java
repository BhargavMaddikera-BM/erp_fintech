package com.blackstrawai.response.ap;

import com.blackstrawai.ap.payment.noncore.PaymentAdviceVo;
import com.blackstrawai.common.BaseResponse;

public class PaymentAdviceResponse extends BaseResponse {
	private PaymentAdviceVo data;

	public PaymentAdviceVo getData() {
		return data;
	}

	public void setData(PaymentAdviceVo data) {
		this.data = data;
	}
}
