package com.blackstrawai.response.settings;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.DepartmentVo;

public class ListDepartmentResponse extends BaseResponse{

	private List<DepartmentVo> data;

	public List<DepartmentVo> getData() {
		return data;
	}

	public void setData(List<DepartmentVo> data) {
		this.data = data;
	}
	
	
}
