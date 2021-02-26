package com.blackstrawai.onboarding.loginandregistration;

import java.util.ArrayList;
import java.util.List;

import com.blackstrawai.common.TokenVo;

public class ApplicationVo extends TokenVo{
	
	private List<ModuleVo>modules=new ArrayList<ModuleVo>();
	
	private int id;
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
	public List<ModuleVo> getModules() {
		return modules;
	}
	public void setModules(List<ModuleVo> modules) {
		this.modules = modules;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	private String name;
	private String description;
	private String url;
	private String status;

}
