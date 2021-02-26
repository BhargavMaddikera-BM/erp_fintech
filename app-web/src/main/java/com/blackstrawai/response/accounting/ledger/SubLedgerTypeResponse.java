package com.blackstrawai.response.accounting.ledger;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsLevel6Vo;

public class SubLedgerTypeResponse  extends BaseResponse{
	
	private List<MinimalChartOfAccountsLevel6Vo> data;

	public List<MinimalChartOfAccountsLevel6Vo> getData() {
		return data;
	}

	public void setData(List<MinimalChartOfAccountsLevel6Vo> data) {
		this.data = data;
	}

}
