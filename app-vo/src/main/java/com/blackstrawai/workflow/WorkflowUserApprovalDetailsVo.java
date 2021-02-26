package com.blackstrawai.workflow;

public class WorkflowUserApprovalDetailsVo {
	private int approvalId;
	private String roleName;
	private String moduleName;
	private int moduleId;
	private String reference;
	private String status;
	private String value;
	private String date;
	private String organizationName;
	private String componentName;
	private int rejectionTypeId;
	
	
	public String getComponentName() {
		return componentName;
	}
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
	public int getModuleId() {
		return moduleId;
	}
	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}
	public int getApprovalId() {
		return approvalId;
	}
	public void setApprovalId(int approvalId) {
		this.approvalId = approvalId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public int getRejectionTypeId() {
		return rejectionTypeId;
	}
	public void setRejectionTypeId(int rejectionTypeId) {
		this.rejectionTypeId = rejectionTypeId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	@Override
	public String toString() {
		return "WorkflowUserApprovalDetailsVo [roleName=" + roleName + ", moduleName=" + moduleName + ", reference="
				+ reference + ", status=" + status + ", value=" + value + ", date=" + date + ", organizationName="
				+ organizationName + "]";
	}
	
	
}