package com.blackstrawai.response.payroll;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.payroll.PayFrequencyListVo;

public class ListPayPeriodResponse extends BaseResponse {

	private List<PayFrequencyListVo> data;

	public List<PayFrequencyListVo> getData() {
		return data;
	}

	public void setData(List<PayFrequencyListVo> data) {
		this.data = data;
	}

}
