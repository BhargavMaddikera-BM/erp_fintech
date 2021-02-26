package com.blackstrawai.keycontact.dropdowns;

import java.util.List;

import com.blackstrawai.ap.dropdowns.BasicDepartmentVo;
import com.blackstrawai.ap.dropdowns.BasicEmployeeTypeVo;
import com.blackstrawai.ap.dropdowns.BasicEmployeeVo;
import com.blackstrawai.common.BaseVo;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsVo;

public class EmployeeDropDownVo extends BaseVo{
	private List<BasicDepartmentVo> department;
	private List<BasicEmployeeTypeVo> employeeType;
	private List<BasicEmployeeVo> employee;
	public List<MinimalChartOfAccountsVo> getLedger() {
		return ledger;
	}
	public void setLedger(List<MinimalChartOfAccountsVo> ledger) {
		this.ledger = ledger;
	}
	private List<MinimalChartOfAccountsVo> ledger;
	
	public List<BasicDepartmentVo> getDepartment() {
		return department;
	}
	public void setDepartment(List<BasicDepartmentVo> department) {
		this.department = department;
	}
	public List<BasicEmployeeTypeVo> getEmployeeType() {
		return employeeType;
	}
	public void setEmployeeType(List<BasicEmployeeTypeVo> employeeType) {
		this.employeeType = employeeType;
	}
	public List<BasicEmployeeVo> getEmployee() {
		return employee;
	}
	public void setEmployee(List<BasicEmployeeVo> employee) {
		this.employee = employee;
	}
	
}
