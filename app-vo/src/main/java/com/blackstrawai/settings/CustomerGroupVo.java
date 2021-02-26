package com.blackstrawai.settings;

import java.sql.Timestamp;

import com.blackstrawai.common.BaseVo;

public class CustomerGroupVo extends BaseVo{
	
	private String name;
	private String description;
	private Integer organizationId;
	private String status;
	private Timestamp createTs;
	private Timestamp updateTs;
	private Integer id;
	private Boolean isSuperAdmin;
	private String roleName="Super Admin";
	
	
	
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Integer getOrganizationId() {
		return organizationId;
	}
	public Boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}
	public void setIsSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	
	

}
