package com.blackstrawai.externalintegration.compliance.employeeprovidentfund;

import com.blackstrawai.common.TokenVo;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EmployeeProvidentFundLoginVo extends TokenVo {
	private int id;
	private int organizationId;
	private String userId;
	private String roleName;
	private String loginName;
	private String loginPassword;
	@JsonProperty
	private boolean rememberMe;
	
	public int getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getLoginPassword() {
		return loginPassword;
	}
	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
	public boolean isRememberMe() {
		return rememberMe;
	}
	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}



}
