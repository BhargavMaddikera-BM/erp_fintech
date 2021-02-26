package com.blackstrawai.vendorsettings;

import java.util.List;

import com.blackstrawai.common.BaseVo;

public class VamBasicVendorVo extends BaseVo {

	private Integer vendorId;
	private String vendorName;
	private String status;
	private String settingsName;
	private String vendorEmail;
	private List<String> contactsEmail;

	public String getVendorEmail() {
		return vendorEmail;
	}

	public void setVendorEmail(String vendorEmail) {
		this.vendorEmail = vendorEmail;
	}

	public List<String> getContactsEmail() {
		return contactsEmail;
	}

	public void setContactsEmail(List<String> contactsEmail) {
		this.contactsEmail = contactsEmail;
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
