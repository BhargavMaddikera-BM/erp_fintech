package com.blackstrawai.response.accounting;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.chartofaccounts.ChartOfAccountsLedgerDetailsVo;

public class LedgerDetailsResponse extends BaseResponse{
	private ChartOfAccountsLedgerDetailsVo data;

	public ChartOfAccountsLedgerDetailsVo getData() {
		return data;
	}

	public void setData(ChartOfAccountsLedgerDetailsVo data) {
		this.data = data;
	}
}
