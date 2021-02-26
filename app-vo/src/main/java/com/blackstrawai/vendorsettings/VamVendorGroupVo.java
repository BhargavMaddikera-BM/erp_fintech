package com.blackstrawai.vendorsettings;

import com.blackstrawai.common.BaseVo;

public class VamVendorGroupVo extends BaseVo {

	private Integer vendorGroupId;
	private String status;
	private Integer seetingsId;
	private int orgId;
	private boolean isSuperAdmin;

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

	public Integer getSeetingsId() {
		return seetingsId;
	}

	public void setSeetingsId(Integer seetingsId) {
		this.seetingsId = seetingsId;
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public boolean isSuperAdmin() {
		return isSuperAdmin;
	}

	public void setSuperAdmin(boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

}
