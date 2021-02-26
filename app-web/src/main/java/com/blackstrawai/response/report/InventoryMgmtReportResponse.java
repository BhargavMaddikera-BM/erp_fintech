package com.blackstrawai.response.report;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.report.InventoryMgmtReportVo;

public class InventoryMgmtReportResponse extends BaseResponse {
	
	private InventoryMgmtReportVo data;

	public InventoryMgmtReportVo getData() {
		return data;
	}

	public void setData(InventoryMgmtReportVo data) {
		this.data = data;
	}
	
	

}
