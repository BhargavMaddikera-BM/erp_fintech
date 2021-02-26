package com.blackstrawai.response.workflow;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.workflow.WorkflowSettingsVo;

public class ListWorkflowSettingsResponse extends BaseResponse{

	private List<WorkflowSettingsVo> data;

	public List<WorkflowSettingsVo> getData() {
		return data;
	}

	public void setData(List<WorkflowSettingsVo> data) {
		this.data = data;
	}

	
	
}
