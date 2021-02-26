package com.blackstrawai.response.externalintegration.compliance;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.compliance.employeeprovidentfund.EmployeeProvidentFundChallanVo;

public class EmployeeProvidentFundChallanResponse extends BaseResponse {
	private List<EmployeeProvidentFundChallanVo> data;

	public List<EmployeeProvidentFundChallanVo> getData() {
		return data;
	}

	public void setData(List<EmployeeProvidentFundChallanVo> data) {
		this.data = data;
	}
}