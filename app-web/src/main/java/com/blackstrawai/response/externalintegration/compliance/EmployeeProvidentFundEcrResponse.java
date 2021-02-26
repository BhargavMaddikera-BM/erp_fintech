package com.blackstrawai.response.externalintegration.compliance;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.compliance.employeeprovidentfund.EmployeeProvidentFundEcrVo;

public class EmployeeProvidentFundEcrResponse extends BaseResponse {
	private List<EmployeeProvidentFundEcrVo> data;

	public List<EmployeeProvidentFundEcrVo> getData() {
		return data;
	}

	public void setData(List<EmployeeProvidentFundEcrVo> data) {
		this.data = data;
	}
}
