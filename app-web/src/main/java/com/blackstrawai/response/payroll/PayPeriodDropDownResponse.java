package com.blackstrawai.response.payroll;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.payroll.dropdowns.PayPeriodDropDownVo;

public class PayPeriodDropDownResponse extends BaseResponse {
	private PayPeriodDropDownVo data;

	public PayPeriodDropDownVo getData() {
		return data;
	}

	public void setData(PayPeriodDropDownVo data) {
		this.data = data;
	}
	
	
}
