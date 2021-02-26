package com.blackstrawai.response.report;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.report.dropdowns.ReportsPeriodDropDownVo;

public class ReportPeriodDropDownResponse extends BaseResponse {
	
	private ReportsPeriodDropDownVo data;

	public ReportsPeriodDropDownVo getData() {
		return data;
	}

	public void setData(ReportsPeriodDropDownVo data) {
		this.data = data;
	}
	
	

}
