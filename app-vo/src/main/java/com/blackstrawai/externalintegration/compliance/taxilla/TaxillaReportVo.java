package com.blackstrawai.externalintegration.compliance.taxilla;

public class TaxillaReportVo {
	private String username;
	private String stateCd;
	private String gstin;
	private String retPeriod;
	private String gstType;
	private String gstSearchType;
	private int organizationId;
	private String userId;
	private String roleName;
	
	public int getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getStateCd() {
		return stateCd;
	}
	public void setStateCd(String stateCd) {
		this.stateCd = stateCd;
	}
	public String getGstin() {
		return gstin;
	}
	public void setGstin(String gstin) {
		this.gstin = gstin;
	}
	public String getRetPeriod() {
		return retPeriod;
	}
	public void setRetPeriod(String retPeriod) {
		this.retPeriod = retPeriod;
	}
	public String getGstType() {
		return gstType;
	}
	public void setGstType(String gstType) {
		this.gstType = gstType;
	}
	public String getGstSearchType() {
		return gstSearchType;
	}
	public void setGstSearchType(String gstSearchType) {
		this.gstSearchType = gstSearchType;
	}

}
