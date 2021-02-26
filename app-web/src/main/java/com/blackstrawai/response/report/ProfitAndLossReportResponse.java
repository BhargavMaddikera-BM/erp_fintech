package com.blackstrawai.response.report;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.report.ProfitAndLossReportVo;


public class ProfitAndLossReportResponse extends BaseResponse {
	
	private ProfitAndLossReportVo data;

	public ProfitAndLossReportVo getData() {
		return data;
	}

	public void setData(ProfitAndLossReportVo data) {
		this.data = data;
	}


}
