package com.blackstrawai.onboarding.organization;

import com.blackstrawai.common.BaseVo;

public class OrganizationLimitedLiabilityOtherDetailsVo extends BaseVo{
	
	private int id;
	private String dateFormat;
	private String timeZone;
	private String contactNumber;
	private String emailId;
	private String taxPanNumber;
	private String gstNumber;
	private String ieCodeNumber;
	private String reportCash;
	private String incorporationDate;
	private String llpin;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	public String getTimeZone() {
		return timeZone;
	}
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getTaxPanNumber() {
		return taxPanNumber;
	}
	public void setTaxPanNumber(String taxPanNumber) {
		this.taxPanNumber = taxPanNumber;
	}
	public String getGstNumber() {
		return gstNumber;
	}
	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}
	public String getIeCodeNumber() {
		return ieCodeNumber;
	}
	public void setIeCodeNumber(String ieCodeNumber) {
		this.ieCodeNumber = ieCodeNumber;
	}
	
	
	public String getReportCash() {
		return reportCash;
	}
	public void setReportCash(String reportCash) {
		this.reportCash = reportCash;
	}
	public String getIncorporationDate() {
		return incorporationDate;
	}
	public void setIncorporationDate(String incorporationDate) {
		this.incorporationDate = incorporationDate;
	}
	public String getLlpin() {
		return llpin;
	}
	public void setLlpin(String llpin) {
		this.llpin = llpin;
	}
	
}
