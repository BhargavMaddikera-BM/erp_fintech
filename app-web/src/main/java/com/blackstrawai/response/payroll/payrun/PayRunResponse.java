package com.blackstrawai.response.payroll.payrun;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.payroll.payrun.PayRunVo;

public class PayRunResponse extends BaseResponse {
	
	private PayRunVo data;

	public PayRunVo getData() {
		return data;
	}

	public void setData(PayRunVo data) {
		this.data = data;
	}
	
	

}
