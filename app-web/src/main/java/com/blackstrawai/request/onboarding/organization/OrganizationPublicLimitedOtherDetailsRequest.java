package com.blackstrawai.request.onboarding.organization;

import com.blackstrawai.common.BaseRequest;

public class OrganizationPublicLimitedOtherDetailsRequest extends BaseRequest{
	
	
	private int id;
	private String dateFormat;
	private String timeZone;
	private String contactNumber;
	private String emailId;
	private String taxPanNumber;
	private String gstNumber;
	
	public String getReportCash() {
		return reportCash;
	}
	public void setReportCash(String reportCash) {
		this.reportCash = reportCash;
	}
	private String ieCodeNumber;
	private String reportCash;
	private String cin;
	private String incorporationDate;
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
	
	public String getCin() {
		return cin;
	}
	public void setCin(String cin) {
		this.cin = cin;
	}
	public String getIncorporationDate() {
		return incorporationDate;
	}
	public void setIncorporationDate(String incorporationDate) {
		this.incorporationDate = incorporationDate;
	}

}
