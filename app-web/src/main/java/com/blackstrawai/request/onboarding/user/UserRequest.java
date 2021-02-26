package com.blackstrawai.request.onboarding.user;

import com.blackstrawai.common.BaseRequest;
import com.blackstrawai.request.onboarding.loginandregistration.RegisteredAddressRequest;

public class UserRequest extends BaseRequest{

	private String firstName;
	private String lastName;
	private String emailId;
	private String phoneNo;
	private String gender;
	private String status;
	private int id;
	private int organizationId;
	private String accessData;
	private String roleName;
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getAccessData() {
		return accessData;
	}
	public void setAccessData(String accessData) {
		this.accessData = accessData;
	}
	public int getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	private RegisteredAddressRequest registeredAddress;
	public RegisteredAddressRequest getRegisteredAddress() {
		return registeredAddress;
	}
	public void setRegisteredAddress(RegisteredAddressRequest registeredAddress) {
		this.registeredAddress = registeredAddress;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	private String dob;
	private int roleId;
	
}
