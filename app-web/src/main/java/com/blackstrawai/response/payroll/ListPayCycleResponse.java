package com.blackstrawai.response.payroll;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.payroll.PayCycleVo;

public class ListPayCycleResponse extends BaseResponse {
	private List<PayCycleVo> data;

	public List<PayCycleVo> getData() {
		return data;
	}

	public void setData(List<PayCycleVo> data) {
		this.data = data;
	}
}
