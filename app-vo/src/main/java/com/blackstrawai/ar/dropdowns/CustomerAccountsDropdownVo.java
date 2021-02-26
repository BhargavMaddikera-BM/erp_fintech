package com.blackstrawai.ar.dropdowns;

import java.util.List;

import com.blackstrawai.keycontact.customer.CustomerVo;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsVo;

public class CustomerAccountsDropdownVo {

	
	private List<MinimalChartOfAccountsVo> discountAndAdjAccounts;
	private List<CustomerVo> customers;
	private List<MinimalChartOfAccountsVo> chartOfAccounts;
	public List<MinimalChartOfAccountsVo> getDiscountAndAdjAccounts() {
		return discountAndAdjAccounts;
	}
	public void setDiscountAndAdjAccounts(List<MinimalChartOfAccountsVo> discountAndAdjAccounts) {
		this.discountAndAdjAccounts = discountAndAdjAccounts;
	}
	public List<CustomerVo> getCustomers() {
		return customers;
	}
	public void setCustomers(List<CustomerVo> customers) {
		this.customers = customers;
	}
	public List<MinimalChartOfAccountsVo> getChartOfAccounts() {
		return chartOfAccounts;
	}
	public void setChartOfAccounts(List<MinimalChartOfAccountsVo> chartOfAccounts) {
		this.chartOfAccounts = chartOfAccounts;
	}	
	
	
}
