package com.blackstrawai.response.workflow;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.CommonVo;

public class ApprovalDropDownResponse extends BaseResponse{

	private List<CommonVo> data;

	public List<CommonVo> getData() {
		return data;
	}

	public void setData(List<CommonVo> data) {
		this.data = data;
	}

	
	
}
