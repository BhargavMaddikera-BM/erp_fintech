package com.blackstrawai.response.payroll;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.payroll.dropdowns.PayTypeDropDownVo;

public class PayTypeDropDownResponse extends BaseResponse{
	
	private  PayTypeDropDownVo data;

	public PayTypeDropDownVo getData() {
		return data;
	}

	public void setData(PayTypeDropDownVo data) {
		this.data = data;
	}

	
}
