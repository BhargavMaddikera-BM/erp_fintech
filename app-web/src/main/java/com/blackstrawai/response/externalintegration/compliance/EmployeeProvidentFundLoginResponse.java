package com.blackstrawai.response.externalintegration.compliance;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.compliance.employeeprovidentfund.EmployeeProvidentFundLoginVo;

public class EmployeeProvidentFundLoginResponse extends BaseResponse {
	private EmployeeProvidentFundLoginVo data;

	public EmployeeProvidentFundLoginVo getData() {
		return data;
	}

	public void setData(EmployeeProvidentFundLoginVo data) {
		this.data = data;
	}

	
}
