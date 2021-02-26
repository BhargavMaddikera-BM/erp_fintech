package com.blackstrawai.onboarding.organization;

import java.sql.Timestamp;

public class BasicOrganizationVo {
	
	private int id;
	
	private String name;
	
	private Timestamp createTs;
	
	private int numberOfApplications;
	
	private int numberOfUsers;
	
	private String status;
	
	private String dateFormat;
	
	private String roleName;
	
	private Integer keyId;
	
	private String access;
	
	private String keyIdStatus;
	
	private boolean isDefault;
	
	
	public boolean isDefault() {
		return isDefault;
	}
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	public String getKeyIdStatus() {
		return keyIdStatus;
	}
	public void setKeyIdStatus(String keyIdStatus) {
		this.keyIdStatus = keyIdStatus;
	}
	public String getAccess() {
		return access;
	}
	public void setAccess(String access) {
		this.access = access;
	}
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public Timestamp getCreateTs() {
		return createTs;
	}
	public void setCreateTs(Timestamp createTs) {
		this.createTs = createTs;
	}
	public int getNumberOfApplications() {
		return numberOfApplications;
	}
	public void setNumberOfApplications(int numberOfApplications) {
		this.numberOfApplications = numberOfApplications;
	}
	public int getNumberOfUsers() {
		return numberOfUsers;
	}
	public void setNumberOfUsers(int numberOfUsers) {
		this.numberOfUsers = numberOfUsers;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Integer getKeyId() {
		return keyId;
	}
	public void setKeyId(Integer keyId) {
		this.keyId = keyId;
	}

	
}
