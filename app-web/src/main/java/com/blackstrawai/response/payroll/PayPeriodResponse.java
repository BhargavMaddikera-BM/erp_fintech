package com.blackstrawai.response.payroll;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.payroll.PayPeriodVo;

public class PayPeriodResponse extends BaseResponse{
	
	private PayPeriodVo data;

	public PayPeriodVo getData() {
		return data;
	}

	public void setData(PayPeriodVo data) {
		this.data = data;
	}
	
	

}
