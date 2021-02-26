package com.blackstrawai.response.report;


import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.report.TrialBalanceReportVo;

public class TrialBalanceReportResponse extends BaseResponse {
	
	private TrialBalanceReportVo data;

	public TrialBalanceReportVo getData() {
		return data;
	}

	public void setData(TrialBalanceReportVo data) {
		this.data = data;
	}


}
