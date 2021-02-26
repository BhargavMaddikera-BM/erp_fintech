package com.blackstrawai.externalintegration.banking.perfios;

public class PerfiosMetadataVo {
	private Long displayIdTypeIdentifiers;
	private String instDesc;
	private Long instId;
	private Long instCode;
	private String instName;
	private boolean isDiscoverySupported;
	private String labelsConfirmPassword;
	private String labelsPassword;
	private String labelsUserId;
	private String mfaRequirement;
	private String displayIdTypeChoices;
	private String displayIdTypeValues;
	
	
	
	public String getDisplayIdTypeChoices() {
		return displayIdTypeChoices;
	}
	public void setDisplayIdTypeChoices(String displayIdTypeChoices) {
		this.displayIdTypeChoices = displayIdTypeChoices;
	}
	public String getDisplayIdTypeValues() {
		return displayIdTypeValues;
	}
	public void setDisplayIdTypeValues(String displayIdTypeValues) {
		this.displayIdTypeValues = displayIdTypeValues;
	}
	public Long getDisplayIdTypeIdentifiers() {
		return displayIdTypeIdentifiers;
	}
	public void setDisplayIdTypeIdentifiers(Long displayIdTypeIdentifiers) {
		this.displayIdTypeIdentifiers = displayIdTypeIdentifiers;
	}
	public String getInstDesc() {
		return instDesc;
	}
	public void setInstDesc(String instDesc) {
		this.instDesc = instDesc;
	}
	public Long getInstId() {
		return instId;
	}
	public void setInstId(Long instId) {
		this.instId = instId;
	}
	public Long getInstCode() {
		return instCode;
	}
	public void setInstCode(Long instCode) {
		this.instCode = instCode;
	}
	public String getInstName() {
		return instName;
	}
	public void setInstName(String instName) {
		this.instName = instName;
	}
	public boolean isDiscoverySupported() {
		return isDiscoverySupported;
	}
	public void setDiscoverySupported(boolean isDiscoverySupported) {
		this.isDiscoverySupported = isDiscoverySupported;
	}
	public String getLabelsConfirmPassword() {
		return labelsConfirmPassword;
	}
	public void setLabelsConfirmPassword(String labelsConfirmPassword) {
		this.labelsConfirmPassword = labelsConfirmPassword;
	}
	public String getLabelsPassword() {
		return labelsPassword;
	}
	public void setLabelsPassword(String labelsPassword) {
		this.labelsPassword = labelsPassword;
	}
	public String getLabelsUserId() {
		return labelsUserId;
	}
	public void setLabelsUserId(String labelsUserId) {
		this.labelsUserId = labelsUserId;
	}
	public String getMfaRequirement() {
		return mfaRequirement;
	}
	public void setMfaRequirement(String mfaRequirement) {
		this.mfaRequirement = mfaRequirement;
	}
	
	
}
