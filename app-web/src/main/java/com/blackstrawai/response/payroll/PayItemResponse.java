package com.blackstrawai.response.payroll;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.payroll.PayItemVo;

public class PayItemResponse extends BaseResponse{
	
	private PayItemVo data;

	public PayItemVo getData() {
		return data;
	}

	public void setData(PayItemVo data) {
		this.data = data;
	}

}
