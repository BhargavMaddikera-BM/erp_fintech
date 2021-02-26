package com.blackstrawai.response.payroll.payrun;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.payroll.dropdowns.PayRunTableDropDownVo;

public class PayRunTableDropDownResponse extends BaseResponse {
	
	private PayRunTableDropDownVo payRunTableDropDownData;

	public PayRunTableDropDownVo getPayRunTableDropDownData() {
		return payRunTableDropDownData;
	}

	public void setPayRunTableDropDownData(PayRunTableDropDownVo payRunTableDropDownData) {
		this.payRunTableDropDownData = payRunTableDropDownData;
	}
	
	

}
