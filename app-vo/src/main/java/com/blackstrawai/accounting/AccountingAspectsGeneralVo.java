package com.blackstrawai.accounting;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AccountingAspectsGeneralVo {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date dateOfCreation;
	private Integer typeId;
	private String journalNo;
	private Integer locationId;
	private Integer currencyId;
	private String notes;
	private String totalDebits="0";
	private String totalCredits="0";
	private String difference;
	private String roleNameTemp;
	public String getRoleNameTemp() {
		return roleNameTemp;
	}

	public void setRoleNameTemp(String roleNameTemp) {
		this.roleNameTemp = roleNameTemp;
	}

	private Boolean isRegisteredLocation;
	private Date create_ts;
	
	public Boolean getIsRegisteredLocation() {
		return isRegisteredLocation;
	}

	public void setIsRegisteredLocation(Boolean isRegisteredLocation) {
		this.isRegisteredLocation = isRegisteredLocation;
	}

	public Date getDateOfCreation() {
		return dateOfCreation;
	}

	public void setDateOfCreation(Date dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getJournalNo() {
		return journalNo;
	}

	public void setJournalNo(String journalNo) {
		this.journalNo = journalNo;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getTotalDebits() {
		return totalDebits;
	}

	public void setTotalDebits(String totalDebits) {
		this.totalDebits = totalDebits;
	}

	public String getTotalCredits() {
		return totalCredits;
	}

	public void setTotalCredits(String totalCredits) {
		this.totalCredits = totalCredits;
	}

	public String getDifference() {
		return difference;
	}

	public void setDifference(String difference) {
		this.difference = difference;
	}

	public Date getCreate_ts() {
		return create_ts;
	}

	public void setCreate_ts(Date create_ts) {
		this.create_ts = create_ts;
	}

	
}
