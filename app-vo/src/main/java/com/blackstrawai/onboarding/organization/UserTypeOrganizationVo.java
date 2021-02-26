package com.blackstrawai.onboarding.organization;

public class UserTypeOrganizationVo extends OrganizationVo{
	
	private boolean isSuperAdmin;
	private boolean isUser;
	private boolean isEmployee;
	private boolean isVendor;
	private boolean isCustomer;
	public boolean isCustomer() {
		return isCustomer;
	}
	public void setCustomer(boolean isCustomer) {
		this.isCustomer = isCustomer;
	}
	public boolean isSuperAdmin() {
		return isSuperAdmin;
	}
	public void setSuperAdmin(boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}
	public boolean isUser() {
		return isUser;
	}
	public void setUser(boolean isUser) {
		this.isUser = isUser;
	}
	public boolean isEmployee() {
		return isEmployee;
	}
	public void setEmployee(boolean isEmployee) {
		this.isEmployee = isEmployee;
	}
	public boolean isVendor() {
		return isVendor;
	}
	public void setVendor(boolean isVendor) {
		this.isVendor = isVendor;
	}
	
	
	
	
	
	
	
	
}
