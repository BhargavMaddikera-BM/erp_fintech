package com.blackstrawai.accounting.dropdowns;

import java.util.List;

import com.blackstrawai.settings.chartofaccounts.ChartOfAccountsEntityVo;
import com.blackstrawai.settings.chartofaccounts.ChartOfAccountsModuleVo;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsLevel3Vo;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsLevel4Vo;

public class LedgerDropDownVo {
	
	public List<MinimalChartOfAccountsLevel3Vo> accountGroup;
	
	public List<MinimalChartOfAccountsLevel4Vo> accountName;
	
	public List<ChartOfAccountsModuleVo> module;
	
	public List<ChartOfAccountsEntityVo> entity;
	
	
	public List<MinimalChartOfAccountsLevel3Vo> getAccountGroup() {
		return accountGroup;
	}
	
	public void setAccountGroup(List<MinimalChartOfAccountsLevel3Vo> accountGroup) {
		this.accountGroup = accountGroup;
	}
	
	public List<MinimalChartOfAccountsLevel4Vo> getAccountName() {
		return accountName;
	}
	
	public void setAccountName(List<MinimalChartOfAccountsLevel4Vo> accountName) {
		this.accountName = accountName;
	}
	
	public List<ChartOfAccountsModuleVo> getModule() {
		return module;
	}
	
	public void setModule(List<ChartOfAccountsModuleVo> module) {
		this.module = module;
	}
	
	public List<ChartOfAccountsEntityVo> getEntity() {
		return entity;
	}
	
	public void setEntity(List<ChartOfAccountsEntityVo> entity) {
		this.entity = entity;
	}



}
