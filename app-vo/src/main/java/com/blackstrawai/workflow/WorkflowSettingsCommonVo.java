package com.blackstrawai.workflow;

public class WorkflowSettingsCommonVo{
	private int id;    
	private String name;
	private String type;
	private int roleId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	@Override
	public String toString() {
		return "WorkflowSettingsCommonVo [id=" + id + ", name=" + name + ", type=" + type + ", roleId=" + roleId + "]";
	}
	
}