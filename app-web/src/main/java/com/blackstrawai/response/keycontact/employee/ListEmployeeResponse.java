package com.blackstrawai.response.keycontact.employee;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.keycontact.employee.EmployeeVo;

public class ListEmployeeResponse extends BaseResponse{
	
	private List<EmployeeVo> data;

	public List<EmployeeVo> getData() {
		return data;
	}

	public void setData(List<EmployeeVo> data) {
		this.data = data;
	}

}
