package com.blackstrawai.onboarding.organization;

import java.util.List;

import com.blackstrawai.common.TokenVo;

public class OrganizationTypeVo{
	
	private int id;
	private String name;
	private String description;
	List<OrganizationIndustryVo>organizationIndustry;
	
	public List<OrganizationIndustryVo> getOrganizationIndustry() {
		return organizationIndustry;
	}
	public void setOrganizationIndustry(List<OrganizationIndustryVo> organizationIndustry) {
		this.organizationIndustry = organizationIndustry;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
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
	

}
