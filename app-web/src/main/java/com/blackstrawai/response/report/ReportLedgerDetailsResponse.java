package com.blackstrawai.response.report;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.report.ReportLedgerDetailsVo;

public class ReportLedgerDetailsResponse extends BaseResponse  {
	
	private ReportLedgerDetailsVo data;

	public ReportLedgerDetailsVo getData() {
		return data;
	}

	public void setData(ReportLedgerDetailsVo data) {
		this.data = data;
	}


}
