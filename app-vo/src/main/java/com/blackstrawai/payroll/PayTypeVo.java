package com.blackstrawai.payroll;

import com.blackstrawai.common.BaseVo;

public class PayTypeVo extends BaseVo{
	
	private String name;
	private String description;
	private int organizationId;	
	private int id;
	private Boolean isSuperAdmin;
	private int parentId;
	private String parentName;
	private String status;
	private int noOfPayItems;
	private String deditOrCredit;
	private Boolean isBase;
	private String roleName="Super Admin";
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Boolean getIsBase() {
		return isBase;
	}
	public void setIsBase(Boolean isBase) {
		this.isBase = isBase;
	}
	public String getDeditOrCredit() {
		return deditOrCredit;
	}
	public void setDeditOrCredit(String deditOrCredit) {
		this.deditOrCredit = deditOrCredit;
	}
	public int getNoOfPayItems() {
		return noOfPayItems;
	}
	public void setNoOfPayItems(int noOfPayItems) {
		this.noOfPayItems = noOfPayItems;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public int getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}
	public void setIsSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
	

}
