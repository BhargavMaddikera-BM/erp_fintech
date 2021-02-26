package com.blackstrawai.vendorsettings;

import com.blackstrawai.common.BaseVo;

public class VamVendorGroupSettingsVo extends BaseVo {

	private String vendorGroupName;
	private Integer vendorGroupId;
	private String status;
	private String settingsName;
	private Integer settingsId;
	private PredefinedSettingsVo settingsData;
	private Integer organizationId;

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public String getSettingsName() {
		return settingsName;
	}

	public void setSettingsName(String settingsName) {
		this.settingsName = settingsName;
	}

	public Integer getSettingsId() {
		return settingsId;
	}

	public void setSettingsId(Integer settingsId) {
		this.settingsId = settingsId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

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

	public PredefinedSettingsVo getSettingsData() {
		return settingsData;
	}

	public void setSettingsData(PredefinedSettingsVo settingsData) {
		this.settingsData = settingsData;
	}

}
