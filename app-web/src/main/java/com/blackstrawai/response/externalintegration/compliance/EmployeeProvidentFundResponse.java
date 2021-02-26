package com.blackstrawai.response.externalintegration.compliance;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.compliance.employeeprovidentfund.EmployeeProvidentFundRefreshVo;

public class EmployeeProvidentFundResponse extends BaseResponse {
	private EmployeeProvidentFundRefreshVo data;

	public EmployeeProvidentFundRefreshVo getData() {
		return data;
	}

	public void setData(EmployeeProvidentFundRefreshVo data) {
		this.data = data;
	}
}
