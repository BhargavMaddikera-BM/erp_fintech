package com.blackstrawai.request.vendorsettings;

public class SettingsModuleRequest {

	private Integer id;
	
	private Integer baseId;
	
	private String module;
	
	private Boolean isActive;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBaseId() {
		return baseId;
	}

	public void setBaseId(Integer baseId) {
		this.baseId = baseId;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SettingsModuleVo [id=");
		builder.append(id);
		builder.append(", baseId=");
		builder.append(baseId);
		builder.append(", module=");
		builder.append(module);
		builder.append(", isActive=");
		builder.append(isActive);
		builder.append("]");
		return builder.toString();
	}
	
	
}
