package com.blackstrawai.request.vendorsettings;

import com.blackstrawai.common.BaseRequest;

public class VamVendorGroupRequest extends BaseRequest {

	private Integer vendorGroupId;
	private String status;
	private Integer settingsId;
	private Integer orgId;
	private boolean isSuperAdmin;

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public boolean isSuperAdmin() {
		return isSuperAdmin;
	}

	public void setSuperAdmin(boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public Integer getVendorGroupId() {
		return vendorGroupId;
	}

	public void setVendorGroupId(Integer vendorGroupId) {
		this.vendorGroupId = vendorGroupId;
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

}
