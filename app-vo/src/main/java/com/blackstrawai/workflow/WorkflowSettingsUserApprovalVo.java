package com.blackstrawai.workflow;

public class WorkflowSettingsUserApprovalVo {
	private int id;
	private int ruleId;
	private String roleName;
	private int userId;
	private int moduleId;
	private int moduleTypeId;
	private int priority;
	private String status;
	private int organizationId;
	public boolean inApp;
	public boolean whatsApp;
	public boolean email;
	private boolean sms;
	
	public boolean isInApp() {
		return inApp;
	}
	public void setInApp(boolean inApp) {
		this.inApp = inApp;
	}
	public boolean isWhatsApp() {
		return whatsApp;
	}
	public void setWhatsApp(boolean whatsApp) {
		this.whatsApp = whatsApp;
	}
	public boolean isEmail() {
		return email;
	}
	public void setEmail(boolean email) {
		this.email = email;
	}
	public boolean isSms() {
		return sms;
	}
	public void setSms(boolean sms) {
		this.sms = sms;
	}
	public int getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}
	public int getModuleId() {
		return moduleId;
	}
	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}
	public int getModuleTypeId() {
		return moduleTypeId;
	}
	public void setModuleTypeId(int moduleTypeId) {
		this.moduleTypeId = moduleTypeId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRuleId() {
		return ruleId;
	}
	public void setRuleId(int ruleId) {
		this.ruleId = ruleId;
	}
	
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "WorkflowSettingsUserApprovalVo [id=" + id + ", ruleId=" + ruleId + ", roleName=" + roleName
				+ ", userId=" + userId + ", moduleId=" + moduleId + ", moduleTypeId=" + moduleTypeId + ", priority="
				+ priority + ", status=" + status + ", organizationId=" + organizationId + ", inApp=" + inApp
				+ ", whatsApp=" + whatsApp + ", email=" + email + ", sms=" + sms + "]";
	}

		
}