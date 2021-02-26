package com.blackstrawai.request.vendorsettings;

public class SettingsTemplateRequest {

	private Integer id;
	
	private Integer baseId;
	
	private String templateType;
	
	private String templateName;
	
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

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
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
		builder.append("SettingsTemplateVo [id=");
		builder.append(id);
		builder.append(", baseId=");
		builder.append(baseId);
		builder.append(", templateType=");
		builder.append(templateType);
		builder.append(", templateName=");
		builder.append(templateName);
		builder.append(", isActive=");
		builder.append(isActive);
		builder.append("]");
		return builder.toString();
	}
	
	
	

}
