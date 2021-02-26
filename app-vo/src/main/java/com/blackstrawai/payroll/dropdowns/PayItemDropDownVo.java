package com.blackstrawai.payroll.dropdowns;

import java.util.List;

import com.blackstrawai.common.BaseVo;
import com.blackstrawai.payroll.BasicPayTypeVo;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsVo;

public class PayItemDropDownVo extends BaseVo{
	
	private List<BasicPayTypeVo>payType;
	private List<MinimalChartOfAccountsVo> chartOfAccounts;

	public List<MinimalChartOfAccountsVo> getChartOfAccounts() {
		return chartOfAccounts;
	}

	public void setChartOfAccounts(List<MinimalChartOfAccountsVo> chartOfAccounts) {
		this.chartOfAccounts = chartOfAccounts;
	}

	public List<BasicPayTypeVo> getPayType() {
		return payType;
	}

	public void setPayType(List<BasicPayTypeVo> payType) {
		this.payType = payType;
	}

}
