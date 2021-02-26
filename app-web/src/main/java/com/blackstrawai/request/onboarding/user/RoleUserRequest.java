package com.blackstrawai.request.onboarding.user;

import java.util.List;

public class RoleUserRequest{

	private int roleId;
	private List<String> emailIdList;
	private String message;
	
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public List<String> getEmailIdList() {
		return emailIdList;
	}
	public void setEmailIdList(List<String> emailIdList) {
		this.emailIdList = emailIdList;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
