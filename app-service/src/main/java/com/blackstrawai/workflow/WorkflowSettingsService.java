package com.blackstrawai.workflow;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.common.BaseService;
import com.blackstrawai.common.DropDownDao;
import com.blackstrawai.upload.dropdowns.ModuleTypeDropDownVo;
import com.blackstrawai.workflow.dropdowns.WorkflowSettingsDropDownVo;

@Service
public class WorkflowSettingsService extends BaseService {

	@Autowired
	WorkflowSettingsDao workflowSettingsDao;

	@Autowired
	DropDownDao dropDownDao;
	
	private Logger logger = Logger.getLogger(WorkflowSettingsService.class);

	public WorkflowSettingsVo createWorkflowSettings(WorkflowSettingsVo workflowSettingsVo) throws ApplicationException {
		logger.info("Entry into method:createWorkflowSettings");
		return workflowSettingsDao.createWorkflowSettings(workflowSettingsVo);
	}
	public List<WorkflowSettingsVo> getAllWorkflowSettingssOfAnOrganization(int OrganizationId,int moduleId) throws ApplicationException {
		logger.info("Entry into method:getAllWorkflowSettingssOfAnOrganization");
		List<WorkflowSettingsVo> workflowSettingsList= workflowSettingsDao.getAllWorkflowSettingsOfAnOrganization(OrganizationId,moduleId);
		
		return workflowSettingsList;
	}
	
	public List<WorkflowSettingsVo> getAllActiveWorkflowSettingsOfAnOrganization(int OrganizationId,int moduleId) throws ApplicationException {
		logger.info("Entry into getAllActiveWorkflowSettingssOfAnOrganization");
		List<WorkflowSettingsVo> workflowSettingsList= workflowSettingsDao.getAllActiveWorkflowSettingsOfAnOrganization(OrganizationId,moduleId);
		
		return workflowSettingsList;
	}

	public WorkflowSettingsVo deleteWorkflowSettings(int id,String status) throws ApplicationException {
		logger.info("Entry into method:deleteWorkflowSettings");
		return workflowSettingsDao.deleteWorkflowSettings(id, status);
	}


	public WorkflowSettingsVo updateWorkflowSettings(WorkflowSettingsVo workflowSettingsVo) throws ApplicationException {
		logger.info("Entry into method:updateWorkflowSettings");
		return workflowSettingsDao.updateWorkflowSettings(workflowSettingsVo);
	}
	
	public boolean updateWorkflowSettingPriority(int id,int priority) throws ApplicationException {
		logger.info("Entry into method: updateWorkflowSettingPriority");
		return workflowSettingsDao.updateWorkflowSettingPriority(id,priority);
	}
	
	public WorkflowSettingsVo getWorkflowSettingsById(int id) throws ApplicationException {
		logger.info("Entry into method:getWorkflowSettingsById");
		return workflowSettingsDao.getWorkflowSettingsById(id);
	}
	
	public WorkflowSettingsDropDownVo getWorkflowSettingsDropdownData(int organizationId,int moduleId) throws ApplicationException {
		return dropDownDao.getWorkflowSettingsDropdownData(organizationId,moduleId);
	}
	public ModuleTypeDropDownVo getWorkflowModuleDropDownData() throws ApplicationException {
		return dropDownDao.getAllWorkflowModulesDropDownData();
	}
	
	public void syncWorflow() throws ApplicationException {
		workflowSettingsDao.syncWorflow();
	}
}

