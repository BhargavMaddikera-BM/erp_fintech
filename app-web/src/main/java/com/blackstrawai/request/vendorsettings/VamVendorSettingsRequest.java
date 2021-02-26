package com.blackstrawai.request.vendorsettings;

import java.util.List;

import com.blackstrawai.common.BaseRequest;

public class VamVendorSettingsRequest extends BaseRequest {

	private String vendorName;
	private Integer vendorId;
	private String settingsName;
	private Integer settingsId;
	private List<Integer> contactsId;
	private String status;
	private PredefinedSettingsRequest settingsData;
	private Integer organizationId;

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
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

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getSettingsName() {
		return settingsName;
	}

	public void setSettingsName(String settingsName) {
		this.settingsName = settingsName;
	}

	public List<Integer> getContactsId() {
		return contactsId;
	}

	public void setContactsId(List<Integer> contactsId) {
		this.contactsId = contactsId;
	}

	public PredefinedSettingsRequest getSettingsData() {
		return settingsData;
	}

	public void setSettingsData(PredefinedSettingsRequest settingsData) {
		this.settingsData = settingsData;
	}

}
