package com.blackstrawai.upload;

public class UploadPayrollVo {

	private String employeeId;
	private String employeeName;
	private String payItemName;
	private int organizationId;
	private int journalNo;
	private String userId;
	private String roleName;
	private String payItemValue;
	private String ledgerName;
	private int ledgerId;
	private String type;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLedgerName() {
		return ledgerName;
	}
	public void setLedgerName(String ledgerName) {
		this.ledgerName = ledgerName;
	}
	public int getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(int ledgerId) {
		this.ledgerId = ledgerId;
	}
	
	public String getPayItemValue() {
		return payItemValue;
	}
	public void setPayItemValue(String payItemValue) {
		this.payItemValue = payItemValue;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getPayItemName() {
		return payItemName;
	}
	public void setPayItemName(String payItemName) {
		this.payItemName = payItemName;
	}
	public int getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}
	public int getJournalNo() {
		return journalNo;
	}
	public void setJournalNo(int journalNo) {
		this.journalNo = journalNo;
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
	@Override
	public String toString() {
		return "UploadPayrollVo [employeeId=" + employeeId + ", employeeName=" + employeeName + ", payItemName="
				+ payItemName + ", organizationId=" + organizationId + ", journalNo=" + journalNo + ", userId=" + userId
				+ ", roleName=" + roleName + ", payItemValue=" + payItemValue + ", ledgerName=" + ledgerName
				+ ", ledgerId=" + ledgerId + ", type=" + type + "]";
	}
	
	
}
