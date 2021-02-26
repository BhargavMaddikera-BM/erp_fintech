package com.blackstrawai.response.payroll;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.payroll.PayTypeVo;

public class PayTypeResponse extends BaseResponse{
	
	private PayTypeVo data;

	public PayTypeVo getData() {
		return data;
	}

	public void setData(PayTypeVo data) {
		this.data = data;
	}

}
