package com.blackstrawai.settings;

import java.sql.Timestamp;

import com.blackstrawai.common.TokenVo;

public class ListPageCustomizationVo extends TokenVo{
	
	
	private Integer organizationId;
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	private String status;
	private Timestamp createTs;
	private String roleName="Super Admin";
	private String moduleName;
	private String data;
	private String updateUserId;
	private String updateRoleName;
	public String getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	public String getUpdateRoleName() {
		return updateRoleName;
	}
	public void setUpdateRoleName(String updateRoleName) {
		this.updateRoleName = updateRoleName;
	}
	public Integer getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
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
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	

}
