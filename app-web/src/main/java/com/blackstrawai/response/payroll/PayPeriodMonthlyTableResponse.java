package com.blackstrawai.response.payroll;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.payroll.PayPeriodMonthlyTableVo;

public class PayPeriodMonthlyTableResponse extends BaseResponse {
	private List<PayPeriodMonthlyTableVo> data;

	public List<PayPeriodMonthlyTableVo> getData() {
		return data;
	}

	public void setData(List<PayPeriodMonthlyTableVo> data) {
		this.data = data;
	}
	
	
}
