package com.blackstrawai.ap.dropdowns;

import com.blackstrawai.common.BaseVo;

public class BasicCustomerGroupVo extends BaseVo{

	private String name;
	private Integer organizationId;
	private Integer id;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getOrganizationId() {
		return organizationId;
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
	
}
