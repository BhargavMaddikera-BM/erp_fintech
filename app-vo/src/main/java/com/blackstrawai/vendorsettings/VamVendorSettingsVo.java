package com.blackstrawai.vendorsettings;

import java.util.List;

import com.blackstrawai.common.BaseVo;

public class VamVendorSettingsVo extends BaseVo {

	private String vendorName;
	private Integer vendorId;
	private String vendorEmail;
	private List<Integer> contactsId;
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

	public String getVendorEmail() {
		return vendorEmail;
	}

	public void setVendorEmail(String vendorEmail) {
		this.vendorEmail = vendorEmail;
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

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public List<Integer> getContactsId() {
		return contactsId;
	}

	public void setContactsId(List<Integer> contactsId) {
		this.contactsId = contactsId;
	}

	public PredefinedSettingsVo getSettingsData() {
		return settingsData;
	}

	public void setSettingsData(PredefinedSettingsVo settingsData) {
		this.settingsData = settingsData;
	}

}
