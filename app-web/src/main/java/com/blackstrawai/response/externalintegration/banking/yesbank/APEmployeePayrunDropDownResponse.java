package com.blackstrawai.response.externalintegration.banking.yesbank;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.yesbank.dropdowns.APEmployeesPayRunDropDownVo;

public class APEmployeePayrunDropDownResponse extends BaseResponse{
	 private List<APEmployeesPayRunDropDownVo> data;

	public List<APEmployeesPayRunDropDownVo> getData() {
		return data;
	}

	public void setData(List<APEmployeesPayRunDropDownVo> data) {
		this.data = data;
	}
	 
	 
	 
}
