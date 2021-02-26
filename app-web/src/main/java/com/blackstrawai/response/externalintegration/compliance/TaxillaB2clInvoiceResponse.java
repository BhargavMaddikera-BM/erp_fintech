package com.blackstrawai.response.externalintegration.compliance;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.compliance.taxilla.B2clInvoiceVo;

public class TaxillaB2clInvoiceResponse extends BaseResponse {
	private List<B2clInvoiceVo> data;

	public List<B2clInvoiceVo> getData() {
		return data;
	}

	public void setData(List<B2clInvoiceVo> data) {
		this.data = data;
	}
}
