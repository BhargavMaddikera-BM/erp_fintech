package com.blackstrawai.vendorsettings;

import java.util.List;

public class VamVendorVo {

	private Integer vendorId;
	private String status;
	private Integer settingId;
	private List<Integer> contactsId;
	private String userId;
	private boolean isSuperAdmin;
	private Integer orgId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean isSuperAdmin() {
		return isSuperAdmin;
	}

	public void setSuperAdmin(boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getSettingId() {
		return settingId;
	}

	public void setSettingId(Integer settingId) {
		this.settingId = settingId;
	}

	public List<Integer> getContactsId() {
		return contactsId;
	}

	public void setContactsId(List<Integer> contactsId) {
		this.contactsId = contactsId;
	}

}
