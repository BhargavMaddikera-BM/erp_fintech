package com.blackstrawai.vendorsettings;

import com.blackstrawai.common.BaseVo;

public class VamBasicVendorGroupVo extends BaseVo {

	private Integer vendorGroupId;
	private String vendorGroupName;
	private String status;
	private String settingsName;

	public Integer getVendorGroupId() {
		return vendorGroupId;
	}

	public void setVendorGroupId(Integer vendorGroupId) {
		this.vendorGroupId = vendorGroupId;
	}

	public String getVendorGroupName() {
		return vendorGroupName;
	}

	public void setVendorGroupName(String vendorGroupName) {
		this.vendorGroupName = vendorGroupName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSettingsName() {
		return settingsName;
	}

	public void setSettingsName(String settingsName) {
		this.settingsName = settingsName;
	}

}
