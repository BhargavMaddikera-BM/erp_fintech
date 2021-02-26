package com.blackstrawai.workflow;

import java.util.List;

import org.apache.log4j.Logger;

import com.blackstrawai.ApplicationException;

public class WorkflowThread extends Thread {
	private Logger logger = Logger.getLogger(WorkflowThread.class);
	private String module;
	private int moduleTypeId;
	private List<WorkflowSettingsVo> applicableRules;
	private WorkflowProcessService workflowProcessService;
	private String workFlowOperation;

	public WorkflowThread(String module, int moduleTypeId, 
			WorkflowProcessService workflowProcessService,List<WorkflowSettingsVo> applicableRules, String workFlowOperation) {
		super();
		this.module = module;
		this.applicableRules=applicableRules;
		this.moduleTypeId = moduleTypeId;
		this.workflowProcessService = workflowProcessService;
		this.workFlowOperation = workFlowOperation;
	}

	

	@Override
	public void run() {
		try {
			switch(workFlowOperation) {
			case WorkflowConstants.WORKFLOW_OPERATION_CREATE:
				createWorkflow();
				break;
			case WorkflowConstants.WORKFLOW_OPERATION_UDATE:
				createWorkflow();
				break;

			}
		} catch (ApplicationException e) {
		}
	}


	private void createWorkflow() throws ApplicationException {
		
		//Create Approvers
		List<WorkflowSettingsUserApprovalVo> approvers = workflowProcessService.createApprovers(applicableRules, moduleTypeId);
		//Update Current Rule
		if(approvers!=null && !approvers.isEmpty()){	
			workflowProcessService.updateCurrentRule(approvers.get(0),false);
		}
	}

}
