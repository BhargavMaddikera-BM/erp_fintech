package com.blackstrawai.response.payroll.payrun;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.payroll.dropdowns.PayRunPeriodDropDownVo;

public class PayRunPeriodDropdownResponse extends BaseResponse {
	
	PayRunPeriodDropDownVo data;

	public PayRunPeriodDropDownVo getData() {
		return data;
	}

	public void setData(PayRunPeriodDropDownVo data) {
		this.data = data;
	}


	

}
