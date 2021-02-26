package com.blackstrawai.response.keycontact.employee;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.keycontact.employee.EmployeeBasicDetailsVo;

public class EmployeeBasicDetailsResponse extends BaseResponse{

	List<EmployeeBasicDetailsVo> data;

	public List<EmployeeBasicDetailsVo> getData() {
		return data;
	}

	public void setData(List<EmployeeBasicDetailsVo> data) {
		this.data = data;
	}
	
	
}
