package com.blackstrawai.response.ap.payment.noncore;

import com.blackstrawai.ap.payment.noncore.PaymentNonCoreVo;
import com.blackstrawai.common.BaseResponse;

public class PaymentNonCoreResponse extends BaseResponse{
	private PaymentNonCoreVo data;

	public PaymentNonCoreVo getData() {
		return data;
	}

	public void setData(PaymentNonCoreVo data) {
		this.data = data;
	}
}
