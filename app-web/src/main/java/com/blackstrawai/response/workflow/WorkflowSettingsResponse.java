package com.blackstrawai.response.workflow;



import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.workflow.WorkflowSettingsVo;

public class WorkflowSettingsResponse extends BaseResponse{
	
	private WorkflowSettingsVo data;

	public WorkflowSettingsVo getData() {
		return data;
	}

	public void setData(WorkflowSettingsVo data) {
		this.data = data;
	}
	

}
