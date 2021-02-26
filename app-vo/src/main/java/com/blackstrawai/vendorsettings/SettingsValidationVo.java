package com.blackstrawai.vendorsettings;

public class SettingsValidationVo {

	private Integer id;
	
	private Integer baseId;
	
	private String activity;
	
	private String validationRule;
	
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

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getValidationRule() {
		return validationRule;
	}

	public void setValidationRule(String validationRule) {
		this.validationRule = validationRule;
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
		builder.append("SettingsValidationVo [id=");
		builder.append(id);
		builder.append(", baseId=");
		builder.append(baseId);
		builder.append(", activity=");
		builder.append(activity);
		builder.append(", validationRule=");
		builder.append(validationRule);
		builder.append(", isActive=");
		builder.append(isActive);
		builder.append("]");
		return builder.toString();
	}
	
	
}
