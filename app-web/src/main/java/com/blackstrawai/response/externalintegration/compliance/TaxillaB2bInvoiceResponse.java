package com.blackstrawai.response.externalintegration.compliance;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.compliance.taxilla.B2BInvoiceVo;

public class TaxillaB2bInvoiceResponse extends BaseResponse {
	private List<B2BInvoiceVo> data;

	public List<B2BInvoiceVo> getData() {
		return data;
	}

	public void setData(List<B2BInvoiceVo> data) {
		this.data = data;
	}
}
