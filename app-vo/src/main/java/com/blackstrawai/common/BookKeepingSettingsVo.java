package com.blackstrawai.common;

public class BookKeepingSettingsVo {
	
	private int id;
	private String defaultGlCode;
	private String defaultGlName;
	private String gstNumber;
	private int locationId;
	public String getDefaultGlCode() {
		return defaultGlCode;
	}
	public void setDefaultGlCode(String defaultGlCode) {
		this.defaultGlCode = defaultGlCode;
	}
	public String getDefaultGlName() {
		return defaultGlName;
	}
	public void setDefaultGlName(String defaultGlName) {
		this.defaultGlName = defaultGlName;
	}
	public String getGstNumber() {
		return gstNumber;
	}
	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

}
