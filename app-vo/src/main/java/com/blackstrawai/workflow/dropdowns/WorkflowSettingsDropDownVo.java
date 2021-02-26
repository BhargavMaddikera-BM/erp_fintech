package com.blackstrawai.workflow.dropdowns;

import java.util.List;

import com.blackstrawai.ap.dropdowns.BasicLocationVo;
import com.blackstrawai.onboarding.user.RolesVo;
import com.blackstrawai.onboarding.user.UserVo;
import com.blackstrawai.settings.CommonVo;
import com.blackstrawai.workflow.WorkflowRuleConditionVo;

public class WorkflowSettingsDropDownVo {
	private List<RolesVo> rolesList;
	private List<UserVo> usersList;
	private List<CommonVo> approvalTypes;
	private List<WorkflowRuleConditionVo> condition;
	private List<BasicLocationVo> locations;
	private List<CommonVo> vendors;
	private boolean isDefaultRuleActivated;
	
	
	
	
	public boolean isDefaultRuleActivated() {
		return isDefaultRuleActivated;
	}
	public void setDefaultRuleActivated(boolean isDefaultRuleActivated) {
		this.isDefaultRuleActivated = isDefaultRuleActivated;
	}
	public List<BasicLocationVo> getLocations() {
		return locations;
	}
	public void setLocations(List<BasicLocationVo> locations) {
		this.locations = locations;
	}
	public List<CommonVo> getVendors() {
		return vendors;
	}
	public void setVendors(List<CommonVo> vendors) {
		this.vendors = vendors;
	}
	public List<RolesVo> getRolesList() {
		return rolesList;
	}
	public void setRolesList(List<RolesVo> rolesList) {
		this.rolesList = rolesList;
	}
	public List<CommonVo> getApprovalTypes() {
		return approvalTypes;
	}
	public void setApprovalTypes(List<CommonVo> approvalTypes) {
		this.approvalTypes = approvalTypes;
	}

	public List<WorkflowRuleConditionVo> getCondition() {
		return condition;
	}
	public void setCondition(List<WorkflowRuleConditionVo> condition) {
		this.condition = condition;
	}
	public List<UserVo> getUsersList() {
		return usersList;
	}
	public void setUsersList(List<UserVo> usersList) {
		this.usersList = usersList;
	}
	



	
	
}
