package com.blackstrawai.ap.dropdowns;

import java.util.List;

import com.blackstrawai.ap.expense.NatureOfSpendingVO;
import com.blackstrawai.ap.expense.StatusVO;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsVo;

public class ExpenseDropdownVo {
	private List<NatureOfSpendingVO> natureOfSpendingVOList;
	private List<StatusVO> statusVOList;
	private List<BasicEmployeeVo> employee;
	private List<BasicVendorVo> basicVendorVoList;
	private List<BasicCustomerVo> basicCustomerVoList;
	private List<MinimalChartOfAccountsVo> chartOfAccounts;
	private String dateFormat;

	public List<MinimalChartOfAccountsVo> getChartOfAccounts() {
		return chartOfAccounts;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public void setChartOfAccounts(List<MinimalChartOfAccountsVo> chartOfAccounts) {
		this.chartOfAccounts = chartOfAccounts;
	}

	public List<NatureOfSpendingVO> getNatureOfSpendingVOList() {
		return natureOfSpendingVOList;
	}

	public void setNatureOfSpendingVOList(List<NatureOfSpendingVO> natureOfSpendingVOList) {
		this.natureOfSpendingVOList = natureOfSpendingVOList;
	}

	public List<StatusVO> getStatusVOList() {
		return statusVOList;
	}

	public void setStatusVOList(List<StatusVO> statusVOList) {
		this.statusVOList = statusVOList;
	}

	public List<BasicEmployeeVo> getEmployee() {
		return employee;
	}

	public void setEmployee(List<BasicEmployeeVo> employee) {
		this.employee = employee;
	}

	public List<BasicVendorVo> getBasicVendorVoList() {
		return basicVendorVoList;
	}

	public void setBasicVendorVoList(List<BasicVendorVo> basicVendorVoList) {
		this.basicVendorVoList = basicVendorVoList;
	}

	public List<BasicCustomerVo> getBasicCustomerVoList() {
		return basicCustomerVoList;
	}

	public void setBasicCustomerVoList(List<BasicCustomerVo> basicCustomerVoList) {
		this.basicCustomerVoList = basicCustomerVoList;
	}
}
