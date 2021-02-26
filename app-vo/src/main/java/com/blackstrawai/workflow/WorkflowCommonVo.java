package com.blackstrawai.workflow;

public class WorkflowCommonVo {
	private int id;
	private String name;
	private String description;
	private boolean isActive;
	
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
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
	public boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
	@Override
	public String toString() {
		return "WorkflowCommonVo [id=" + id + ", name=" + name + ", description=" + description + ", isActive="
				+ isActive + "]";
	}

	
}