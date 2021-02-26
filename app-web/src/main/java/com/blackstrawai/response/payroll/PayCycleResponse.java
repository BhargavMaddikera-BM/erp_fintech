package com.blackstrawai.response.payroll;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.payroll.PayCycleVo;

public class PayCycleResponse extends BaseResponse {
	private PayCycleVo data;

	public PayCycleVo getData() {
		return data;
	}

	public void setData(PayCycleVo data) {
		this.data = data;
	}
}
