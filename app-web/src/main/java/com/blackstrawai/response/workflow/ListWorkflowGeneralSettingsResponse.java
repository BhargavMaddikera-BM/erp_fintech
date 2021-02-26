package com.blackstrawai.response.workflow;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.workflow.WorkflowGeneralSettingsVo;

public class ListWorkflowGeneralSettingsResponse extends BaseResponse{

	private List<WorkflowGeneralSettingsVo> data;

	public List<WorkflowGeneralSettingsVo> getData() {
		return data;
	}

	public void setData(List<WorkflowGeneralSettingsVo> data) {
		this.data = data;
	}

	
	
}
