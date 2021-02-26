package com.blackstrawai.request.vendorsettings;

import java.util.List;
import java.util.Map;

import com.blackstrawai.common.BaseRequest;

public class PredefinedSettingsRequest extends BaseRequest{
private Integer id;
	
	private String name;
	
	private Boolean isDefault;
	
	private String status;
	
	private Integer organizationId;
	
	private Boolean isSuperAdmin;
	
	private List<SettingsModuleRequest> modules;
	
	//private List<SettingsTemplateVo> template;
	Map<String , List<SettingsTemplateRequest>> template;
	
	private List<SettingsValidationRequest> validation;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}

	public void setIsSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public List<SettingsModuleRequest> getModules() {
		return modules;
	}

	public void setModules(List<SettingsModuleRequest> modules) {
		this.modules = modules;
	}


	public Map<String, List<SettingsTemplateRequest>> getTemplate() {
		return template;
	}

	public void setTemplate(Map<String, List<SettingsTemplateRequest>> template) {
		this.template = template;
	}

	public List<SettingsValidationRequest> getValidation() {
		return validation;
	}

	public void setValidation(List<SettingsValidationRequest> validation) {
		this.validation = validation;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PredefinedSettingsVo [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", isDefault=");
		builder.append(isDefault);
		builder.append(", status=");
		builder.append(status);
		builder.append(", organizationId=");
		builder.append(organizationId);
		builder.append(", isSuperAdmin=");
		builder.append(isSuperAdmin);
		builder.append(", modules=");
		builder.append(modules);
		builder.append(", template=");
		builder.append(template);
		builder.append(", validation=");
		builder.append(validation);
		builder.append("]");
		return builder.toString();
	}
	
	
	
}
