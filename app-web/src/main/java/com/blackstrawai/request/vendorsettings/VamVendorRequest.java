package com.blackstrawai.request.vendorsettings;

import java.util.List;

import com.blackstrawai.common.BaseRequest;

public class VamVendorRequest extends BaseRequest {

	private Integer vendorId;
	private String status;
	private Integer settingsId;
	private List<Integer> contactsId;
	private boolean isSuperAdmin;
	private Integer orgId;

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

	public Integer getSettingsId() {
		return settingsId;
	}

	public void setSettingsId(Integer settingsId) {
		this.settingsId = settingsId;
	}

	public List<Integer> getContactsId() {
		return contactsId;
	}

	public void setContactsId(List<Integer> contactsId) {
		this.contactsId = contactsId;
	}

}
