package com.blackstrawai.response.ap.payment.noncore;

import com.blackstrawai.ap.payment.noncore.PaymentNonCoreBillAndCreditDetailsVo;
import com.blackstrawai.common.BaseResponse;

public class PaymentNonCoreBillResponse extends BaseResponse {
	private PaymentNonCoreBillAndCreditDetailsVo data;

	public PaymentNonCoreBillAndCreditDetailsVo getData() {
		return data;
	}

	public void setData(PaymentNonCoreBillAndCreditDetailsVo data) {
		this.data = data;
	}

	
	
	
}
