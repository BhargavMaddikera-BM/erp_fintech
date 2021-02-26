package com.blackstrawai.response.workflow;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.workflow.WorkflowUserApprovalDetailsVo;

public class ListApprovalResponse extends BaseResponse{

	private List<WorkflowUserApprovalDetailsVo> data;

	public List<WorkflowUserApprovalDetailsVo> getData() {
		return data;
	}

	public void setData(List<WorkflowUserApprovalDetailsVo> data) {
		this.data = data;
	}

	
	
}
