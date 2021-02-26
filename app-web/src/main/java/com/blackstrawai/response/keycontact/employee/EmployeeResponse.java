package com.blackstrawai.response.keycontact.employee;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.keycontact.employee.EmployeeVo;

public class EmployeeResponse extends BaseResponse{

	private EmployeeVo data;

	public EmployeeVo getData() {
		return data;
	}

	public void setData(EmployeeVo data) {
		this.data = data;
	}
	
	
}
