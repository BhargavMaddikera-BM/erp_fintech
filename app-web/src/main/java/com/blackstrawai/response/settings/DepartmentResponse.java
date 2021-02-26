package com.blackstrawai.response.settings;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.DepartmentVo;

public class DepartmentResponse extends BaseResponse{

	private DepartmentVo data;

	public DepartmentVo getData() {
		return data;
	}

	public void setData(DepartmentVo data) {
		this.data = data;
	}
	
	
}
