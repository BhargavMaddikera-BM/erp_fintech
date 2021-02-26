package com.blackstrawai.response.ap;

import java.util.List;

import com.blackstrawai.ar.applycredits.InvoiceDetailsVo;
import com.blackstrawai.common.BaseResponse;

public class PaymentNonCoreInvoiceListResponse extends BaseResponse {
	private List<InvoiceDetailsVo> data;

	public List<InvoiceDetailsVo> getData() {
		return data;
	}

	public void setData(List<InvoiceDetailsVo> data) {
		this.data = data;
	}
	
	
}
