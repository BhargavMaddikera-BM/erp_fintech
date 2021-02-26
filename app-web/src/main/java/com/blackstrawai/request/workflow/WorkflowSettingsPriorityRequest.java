package com.blackstrawai.request.workflow;

import java.util.List;

import com.blackstrawai.common.BaseRequest;
import com.blackstrawai.workflow.WorkflowSettingsPriorityVo;

public class WorkflowSettingsPriorityRequest extends BaseRequest{
	private List<WorkflowSettingsPriorityVo> data;

	public List<WorkflowSettingsPriorityVo> getData() {
		return data;
	}

	public void setData(List<WorkflowSettingsPriorityVo> data) {
		this.data = data;
	}
	
}