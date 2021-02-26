package com.blackstrawai.settings;

import java.sql.Timestamp;

import com.blackstrawai.common.TokenVo;

public class SettingsModuleOrganizationVo extends TokenVo{
private int id;
private String type;
private String name;
private String moduleName;
private String subModuleName;
private boolean isRequired;
private int organizationId;
private String roleName;
private Timestamp createTs;
private Timestamp updateTs;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getModuleName() {
	return moduleName;
}
public void setModuleName(String moduleName) {
	this.moduleName = moduleName;
}
public String getSubModuleName() {
	return subModuleName;
}
public void setSubModuleName(String subModuleName) {
	this.subModuleName = subModuleName;
}
public boolean isRequired() {
	return isRequired;
}
public void setRequired(boolean isRequired) {
	this.isRequired = isRequired;
}
public int getOrganizationId() {
	return organizationId;
}
public void setOrganizationId(int organizationId) {
	this.organizationId = organizationId;
}

public String getRoleName() {
	return roleName;
}
public void setRoleName(String roleName) {
	this.roleName = roleName;
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
	return "SettingsModuleOrganizationVo [id=" + id + ", type=" + type + ", name=" + name + ", moduleName=" + moduleName
			+ ", subModuleName=" + subModuleName + ", isRequired=" + isRequired + ", organizationId=" + organizationId
			+ ", roleName=" + roleName + ", createTs=" + createTs + ", updateTs=" + updateTs + "]";
}


}
