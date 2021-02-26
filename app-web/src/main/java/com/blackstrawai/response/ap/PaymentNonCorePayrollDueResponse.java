package com.blackstrawai.response.ap;

import com.blackstrawai.ap.payment.noncore.PayrollDueVo;
import com.blackstrawai.common.BaseResponse;

public class PaymentNonCorePayrollDueResponse extends BaseResponse {
	private PayrollDueVo data;

	public PayrollDueVo getData() {
		return data;
	}

	public void setData(PayrollDueVo data) {
		this.data = data;
	}
}
