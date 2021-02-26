package com.blackstrawai.response.workflow;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.workflow.WorkflowGeneralSettingsVo;

public class WorkflowGeneralSettingsResponse extends BaseResponse{
	
	private WorkflowGeneralSettingsVo data;

	public WorkflowGeneralSettingsVo getData() {
		return data;
	}

	public void setData(WorkflowGeneralSettingsVo data) {
		this.data = data;
	}
	

}
