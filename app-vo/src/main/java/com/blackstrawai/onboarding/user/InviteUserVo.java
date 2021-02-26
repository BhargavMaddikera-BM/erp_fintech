package com.blackstrawai.onboarding.user;

import java.util.List;

import com.blackstrawai.common.TokenVo;

public class InviteUserVo extends TokenVo{

	private List<RoleUserVo> roleUser;
	private int orgId;
	private String userId;
	private String roleName;
	private String updateRoleName;
	private int updateUserId;
	private String status;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<RoleUserVo> getRoleUser() {
		return roleUser;
	}
	public void setRoleUser(List<RoleUserVo> roleUser) {
		this.roleUser = roleUser;
	}
	public int getOrgId() {
		return orgId;
	}
	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getUpdateRoleName() {
		return updateRoleName;
	}
	public void setUpdateRoleName(String updateRoleName) {
		this.updateRoleName = updateRoleName;
	}
	public int getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(int updateUserId) {
		this.updateUserId = updateUserId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	

}
