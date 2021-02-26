package com.blackstrawai.response.payroll.payrun;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.payroll.payrun.PayRunVo;

public class PayRunListResponse extends BaseResponse {
	
	private List<PayRunVo> data;

	public List<PayRunVo> getData() {
		return data;
	}

	public void setData(List<PayRunVo> data) {
		this.data = data;
	}

	

	
	
	

}
