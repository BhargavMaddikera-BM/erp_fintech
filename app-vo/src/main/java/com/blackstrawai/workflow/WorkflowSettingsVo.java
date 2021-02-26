package com.blackstrawai.workflow;

import java.sql.Timestamp;

import com.blackstrawai.common.TokenVo;


public class WorkflowSettingsVo extends TokenVo{
	private int id;
	private int moduleId;
	private String name;
	private int priority;
	private WorkflowSettingsRuleData data ;
	private boolean isBase;
	private String description;
	private int organizationId;
	private int updateUserId;
	private String updateRoleName;
	private String roleName;
	private boolean isSuperAdmin;
	private String status;
	private Timestamp createTs;
	private Timestamp updateTs;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getModuleId() {
		return moduleId;
	}
	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public WorkflowSettingsRuleData getData() {
		return data;
	}
	public void setData(WorkflowSettingsRuleData data) {
		this.data = data;
	}
	public boolean getIsBase() {
		return isBase;
	}
	public void setIsBase(boolean isBase) {
		this.isBase = isBase;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}
	public int getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(int updateUserId) {
		this.updateUserId = updateUserId;
	}
	public String getUpdateRoleName() {
		return updateRoleName;
	}
	public void setUpdateRoleName(String updateRoleName) {
		this.updateRoleName = updateRoleName;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public boolean isSuperAdmin() {
		return isSuperAdmin;
	}
	public void setSuperAdmin(boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Timestamp getCreateTs() {
		return createTs;
	}
	public void setCreateTs(Timestamp createTs) {
		this.createTs = createTs;
	}
	public Timestamp getUpdateTs() {
		return updateTs;
	}
	public void setUpdateTs(Timestamp updateTs) {
		this.updateTs = updateTs;
	}
	@Override
	public String toString() {
		return "WorkflowSettingsVo [id=" + id + ", moduleId=" + moduleId + ", name=" + name + ", priority=" + priority
				+ ", data=" + data + ", isBase=" + isBase + ", description=" + description + ", organizationId="
				+ organizationId + ", updateUserId=" + updateUserId + ", updateRoleName=" + updateRoleName
				+ ", roleName=" + roleName + ", isSuperAdmin=" + isSuperAdmin + ", status=" + status + ", createTs="
				+ createTs + ", updateTs=" + updateTs + "]";
	}
	
	
}