package com.blackstrawai.response.keycontact.employee;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.keycontact.dropdowns.EmployeeDropDownVo;

public class EmployeeDropDownResponse extends BaseResponse{

	private EmployeeDropDownVo data;

	public EmployeeDropDownVo getData() {
		return data;
	}

	public void setData(EmployeeDropDownVo data) {
		this.data = data;
	}
	
	
}
