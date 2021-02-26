package com.blackstrawai.response.settings;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.chartofaccounts.ChartOfAccountsReportVo;

public class ListChartOfAccountsReportResponse extends BaseResponse{
	
	private List<ChartOfAccountsReportVo> data;

	public List<ChartOfAccountsReportVo> getData() {
		return data;
	}

	public void setData(List<ChartOfAccountsReportVo> data) {
		this.data = data;
	}

	
}
