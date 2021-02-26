package com.blackstrawai.onboarding.user;

import java.util.List;

import com.blackstrawai.settings.CommonVo;

public class RolesVo {
	private Integer id;
	
	private String name;
	
	private Integer value;
	
	private List<UserVo> users;

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

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public List<UserVo> getUsers() {
		return users;
	}

	public void setUsers(List<UserVo> users) {
		this.users = users;
	}


	
}
