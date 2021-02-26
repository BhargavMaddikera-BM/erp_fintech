package com.blackstrawai.keycontact.dropdowns;

import java.util.List;

import com.blackstrawai.common.BaseVo;
import com.blackstrawai.common.CountryVo;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsVo;

public class StatutoryBodyDropDownVo  extends BaseVo{
	
	private List<MinimalChartOfAccountsVo> chartOfAccounts;
	private List<CountryVo>country;
	public List<MinimalChartOfAccountsVo> getChartOfAccounts() {
		return chartOfAccounts;
	}
	public void setChartOfAccounts(List<MinimalChartOfAccountsVo> chartOfAccounts) {
		this.chartOfAccounts = chartOfAccounts;
	}
	public List<CountryVo> getCountry() {
		return country;
	}
	public void setCountry(List<CountryVo> country) {
		this.country = country;
	}

	

}
