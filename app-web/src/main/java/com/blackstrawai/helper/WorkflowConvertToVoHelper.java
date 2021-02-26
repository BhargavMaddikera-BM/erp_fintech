package com.blackstrawai.helper;

import com.blackstrawai.request.workflow.ApprovalRequest;
import com.blackstrawai.request.workflow.WorkflowGeneralSettingsRequest;
import com.blackstrawai.request.workflow.WorkflowSettingsRequest;
import com.blackstrawai.workflow.ApprovalVo;
import com.blackstrawai.workflow.WorkflowGeneralSettingsVo;
import com.blackstrawai.workflow.WorkflowSettingsVo;

public class WorkflowConvertToVoHelper {

	private static WorkflowConvertToVoHelper workflowConvertToVoHelper;

	public static WorkflowConvertToVoHelper getInstance() {
		if (workflowConvertToVoHelper == null) {
			workflowConvertToVoHelper = new WorkflowConvertToVoHelper();
		}
		return workflowConvertToVoHelper;
	}

	public ApprovalVo convertApprovalVoFromApprovalRequest(ApprovalRequest approvalRequest) {

		ApprovalVo approvalVo=new ApprovalVo();
		approvalVo.setApprovalId(approvalRequest.getApprovalId());
		approvalVo.setOrganizationId(approvalRequest.getOrganizationId());
		approvalVo.setOrganizationName(approvalRequest.getOrganizationName());
		approvalVo.setUserId(approvalRequest.getUserId());
		approvalVo.setRoleName(approvalRequest.getRoleName());
		approvalVo.setStatus(approvalRequest.getStatus());
		approvalVo.setRejectionRemarks(approvalRequest.getRejectionRemarks());
		approvalVo.setRejectionTypeId(approvalRequest.getRejectionTypeId());
		return approvalVo;
	}

	public WorkflowGeneralSettingsVo convertWorkflowGeneralSettingsVoFromWorkflowGeneralSettingsRequest(WorkflowGeneralSettingsRequest workflowGeneralSettingsRequest) {

		WorkflowGeneralSettingsVo workflowGeneralSettingsVo=new WorkflowGeneralSettingsVo();
		workflowGeneralSettingsVo.setId(workflowGeneralSettingsRequest.getId());    
		workflowGeneralSettingsVo.setModuleId(workflowGeneralSettingsRequest.getModuleId());    
		workflowGeneralSettingsVo.setData(workflowGeneralSettingsRequest.getData());    
		workflowGeneralSettingsVo.setOrganizationId(workflowGeneralSettingsRequest.getOrganizationId());    
		workflowGeneralSettingsVo.setUpdateUserId(workflowGeneralSettingsRequest.getUpdateUserId());    
		workflowGeneralSettingsVo.setUpdateRoleName(workflowGeneralSettingsRequest.getUpdateRoleName());    
		workflowGeneralSettingsVo.setRoleName(workflowGeneralSettingsRequest.getRoleName());    
		workflowGeneralSettingsVo.setIsSuperAdmin(workflowGeneralSettingsRequest.getIsSuperAdmin());    
		workflowGeneralSettingsVo.setStatus(workflowGeneralSettingsRequest.getStatus());    
		workflowGeneralSettingsVo.setCreateTs(workflowGeneralSettingsRequest.getCreateTs());    
		workflowGeneralSettingsVo.setUpdateTs(workflowGeneralSettingsRequest.getUpdateTs());    
		return workflowGeneralSettingsVo;
	}

	public WorkflowSettingsVo convertWorkflowSettingsVoFromWorkflowSettingsRequest(
			WorkflowSettingsRequest workflowSettingsRequest) {

		WorkflowSettingsVo workflowSettingsVo = new WorkflowSettingsVo();
		workflowSettingsVo.setId(workflowSettingsRequest.getId());
		workflowSettingsVo.setModuleId(workflowSettingsRequest.getModuleId());
		workflowSettingsVo.setName(workflowSettingsRequest.getName());
		workflowSettingsVo.setPriority(workflowSettingsRequest.getPriority());
		workflowSettingsVo.setData(workflowSettingsRequest.getData());
		workflowSettingsVo.setDescription(workflowSettingsRequest.getDescription());
		workflowSettingsVo.setOrganizationId(workflowSettingsRequest.getOrganizationId());
		workflowSettingsVo.setUpdateUserId(workflowSettingsRequest.getUpdateUserId());
		workflowSettingsVo.setUpdateRoleName(workflowSettingsRequest.getUpdateRoleName());
		workflowSettingsVo.setRoleName(workflowSettingsRequest.getRoleName());
		workflowSettingsVo.setSuperAdmin(workflowSettingsRequest.isSuperAdmin());
		workflowSettingsVo.setStatus(workflowSettingsRequest.getStatus());
		workflowSettingsVo.setCreateTs(workflowSettingsRequest.getCreateTs());
		workflowSettingsVo.setUpdateTs(workflowSettingsRequest.getUpdateTs());
		workflowSettingsVo.setUserId(workflowSettingsRequest.getUserId());
		workflowSettingsVo.setIsBase(workflowSettingsRequest.getIsBase());
		return workflowSettingsVo;
	}


}
