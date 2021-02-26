package com.blackstrawai.onboarding.user;

import java.sql.Timestamp;

import com.blackstrawai.common.TokenVo;

public class UserVo extends TokenVo {

	private String firstName;
	private String lastName;
	private String emailId;
	private String phoneNo;
	private String gender;
	private String status;
	private String roleName;
	private String fullName;
	private String accessData;

	public String getAccessData() {
		return accessData;
	}

	public void setAccessData(String accessData) {
		this.accessData = accessData;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	private int id;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	private Timestamp createTs;
	private Timestamp updateTs;

	public Timestamp getCreateTs() {
		return createTs;
	}

	public void setCreateTs(Timestamp createTs) {
		this.createTs = createTs;
	}

	public Timestamp getUpdateTs() {
		return updateTs;
	}

	public void setUpdateTs(Timestamp updateTs) {
		this.updateTs = updateTs;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public RegisteredAddressVo getRegisteredAddress() {
		return registeredAddress;
	}

	public void setRegisteredAddress(RegisteredAddressVo registeredAddress) {
		this.registeredAddress = registeredAddress;
	}

	private String dob;
	private int roleId;
	private int organizationId;

	public int getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}

	private RegisteredAddressVo registeredAddress;

}
