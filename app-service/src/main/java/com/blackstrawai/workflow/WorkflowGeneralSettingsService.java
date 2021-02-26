package com.blackstrawai.workflow;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.DropDownDao;
import com.blackstrawai.workflow.dropdowns.WorkflowGeneralSettingsDropDownVo;

@Service
public class WorkflowGeneralSettingsService extends BaseService {

	@Autowired
	WorkflowGeneralSettingsDao workflowGeneralSettingsDao;
	
	@Autowired
	DropDownDao dropDownDao;

	private Logger logger = Logger.getLogger(WorkflowGeneralSettingsService.class);

	public WorkflowGeneralSettingsVo createWorkflowGeneralSetting(WorkflowGeneralSettingsVo workflowGeneralSettingsVo) throws ApplicationException {
		logger.info("Entry into method:createWorkflowGeneralSettings");
		return workflowGeneralSettingsDao.createWorkflowGeneralSetting(workflowGeneralSettingsVo);
	}
	public List<WorkflowGeneralSettingsVo> getAllWorkflowGeneralSettingsOfAnOrganization(int OrganizationId,int moduleId) throws ApplicationException {
		logger.info("Entry into getAllWorkflowGeneralSettingsOfAnOrganization");
		List<WorkflowGeneralSettingsVo> workflowGeneralSettingsList= workflowGeneralSettingsDao.getAllWorkflowGeneralSettingsOfAnOrganization(OrganizationId,moduleId);
		
		return workflowGeneralSettingsList;
	}

	public WorkflowGeneralSettingsVo updateWorkflowGeneralSetting(WorkflowGeneralSettingsVo workflowGeneralSettingsVo) throws ApplicationException {
		logger.info("Entry into method:updateWorkflowGeneralSettings");
		return workflowGeneralSettingsDao.updateWorkflowGeneralSetting(workflowGeneralSettingsVo);
	}

	
}

