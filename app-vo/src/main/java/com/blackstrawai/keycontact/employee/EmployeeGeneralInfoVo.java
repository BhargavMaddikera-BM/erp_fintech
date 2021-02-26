package com.blackstrawai.keycontact.employee;

import com.blackstrawai.common.BaseVo;

public class EmployeeGeneralInfoVo extends BaseVo{

	private String employeeId;
	private String firstName;
	private String lastName;
	private String email;
	private String dateOfJoining;
	private String mobileNo;
	private Integer department;
	private Integer reportingTo;
	private String employeeStatus;
	private Integer employeeType;
	
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDateOfJoining() {
		return dateOfJoining;
	}
	public void setDateOfJoining(String dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public Integer getDepartment() {
		return department;
	}
	public void setDepartment(Integer department) {
		this.department = department;
	}
	public Integer getReportingTo() {
		return reportingTo;
	}
	public void setReportingTo(Integer reportingTo) {
		this.reportingTo = reportingTo;
	}
	public String getEmployeeStatus() {
		return employeeStatus;
	}
	public void setEmployeeStatus(String employeeStatus) {
		this.employeeStatus = employeeStatus;
	}
	public Integer getEmployeeType() {
		return employeeType;
	}
	public void setEmployeeType(Integer employeeType) {
		this.employeeType = employeeType;
	}
	
	
}
