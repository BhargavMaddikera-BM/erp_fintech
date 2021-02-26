package com.blackstrawai.response.payroll.payrun;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.payroll.dropdowns.PayRunPaymentTypeDropDownVo;

public class PayRunPaymentTypeDropDownResponse extends BaseResponse {
   
	private PayRunPaymentTypeDropDownVo data;

	public PayRunPaymentTypeDropDownVo getData() {
		return data;
	}

	public void setData(PayRunPaymentTypeDropDownVo data) {
		this.data = data;
	}
	
	
}
