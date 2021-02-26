package com.blackstrawai.vendorsettings.dropdowns;

import java.util.List;

import com.blackstrawai.vendorsettings.VamVendorContactVo;
import com.blackstrawai.vendorsettings.VamVendorNameVo;

public class VamDropDownVo {

	private List<VamVendorNameVo> vendorNames;
	private List<VamVendorContactVo> vendorContacts;
	private List<SettingsDropDownVo> settingsName;
	private List<VamVendorGroupDropDownVo> vendorGroupsName;

	public List<SettingsDropDownVo> getSettingsName() {
		return settingsName;
	}

	public void setSettingsName(List<SettingsDropDownVo> settingsName) {
		this.settingsName = settingsName;
	}

	public List<VamVendorGroupDropDownVo> getVendorGroupsName() {
		return vendorGroupsName;
	}

	public void setVendorGroupsName(List<VamVendorGroupDropDownVo> vendorGroupsName) {
		this.vendorGroupsName = vendorGroupsName;
	}

	public List<VamVendorNameVo> getVendorNames() {
		return vendorNames;
	}

	public void setVendorNames(List<VamVendorNameVo> vendorNames) {
		this.vendorNames = vendorNames;
	}

	public List<VamVendorContactVo> getVendorContacts() {
		return vendorContacts;
	}

	public void setVendorContacts(List<VamVendorContactVo> vendorContacts) {
		this.vendorContacts = vendorContacts;
	}

}
