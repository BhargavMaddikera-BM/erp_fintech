package com.blackstrawai.response.payroll;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.payroll.dropdowns.PayItemDropDownVo;

public class PayItemDropDownResponse extends BaseResponse{
	
	private  PayItemDropDownVo data;

	public PayItemDropDownVo getData() {
		return data;
	}

	public void setData(PayItemDropDownVo data) {
		this.data = data;
	}

}
