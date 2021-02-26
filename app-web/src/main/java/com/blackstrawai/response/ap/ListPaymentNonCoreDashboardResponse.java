package com.blackstrawai.response.ap;

import java.util.List;

import com.blackstrawai.ap.billsinvoice.BillsInvoiceDashboardVo;
import com.blackstrawai.common.BaseResponse;

public class ListPaymentNonCoreDashboardResponse extends BaseResponse {
	private List<BillsInvoiceDashboardVo> data;

	public List<BillsInvoiceDashboardVo> getData() {
		return data;
	}

	public void setData(List<BillsInvoiceDashboardVo> data) {
		this.data = data;
	}
	
	
}
