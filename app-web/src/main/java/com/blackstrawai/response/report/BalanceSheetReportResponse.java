package com.blackstrawai.response.report;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.report.BalanceSheetReportVo;

public class BalanceSheetReportResponse extends BaseResponse{
	
	private BalanceSheetReportVo data;

	public BalanceSheetReportVo getData() {
		return data;
	}

	public void setData(BalanceSheetReportVo data) {
		this.data = data;
	}

}
