package com.blackstrawai.ap.dropdowns;

import com.blackstrawai.common.BaseVo;

public class BasicDepartmentVo extends BaseVo{

	private String Name;
	private Integer id;
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
}
