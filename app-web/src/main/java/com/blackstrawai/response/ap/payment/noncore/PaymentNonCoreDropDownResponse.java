package com.blackstrawai.response.ap.payment.noncore;

import com.blackstrawai.ap.dropdowns.PaymentNonCoreDropDownVo;
import com.blackstrawai.common.BaseResponse;

public class PaymentNonCoreDropDownResponse extends BaseResponse{
	private PaymentNonCoreDropDownVo data;

	public PaymentNonCoreDropDownVo getData() {
		return data;
	}

	public void setData(PaymentNonCoreDropDownVo data) {
		this.data = data;
	}
}
