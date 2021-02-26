package com.blackstrawai.response.payroll.payrun;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.payroll.payrun.PayRunTableVo;

public class PayRunTableResponse  extends BaseResponse {

	private List<PayRunTableVo> data;

	public List<PayRunTableVo> getData() {
		return data;
	}

	public void setData(List<PayRunTableVo> data) {
		this.data = data;
	}
	
	
}
