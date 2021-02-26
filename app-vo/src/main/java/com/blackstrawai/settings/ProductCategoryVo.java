package com.blackstrawai.settings;

import java.util.List;

import com.blackstrawai.common.BaseVo;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsVo;

public class ProductCategoryVo extends BaseVo{

	private Integer id;
	
	private String category;
	
	private String type;
	
	private String roleName;
	
	private Integer organizationId;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	
	
}
