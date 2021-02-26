package com.blackstrawai.response.workflow;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.workflow.dropdowns.WorkflowSettingsDropDownVo;

public class WorkflowSettingsDropDownResponse extends BaseResponse{

	private WorkflowSettingsDropDownVo data;

	public WorkflowSettingsDropDownVo getData() {
		return data;
	}

	public void setData(WorkflowSettingsDropDownVo data) {
		this.data = data;
	}

	
	
}
