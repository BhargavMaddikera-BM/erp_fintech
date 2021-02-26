package com.blackstrawai.response.externalintegration.compliance;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.compliance.taxilla.B2csInvoiceVo;

public class TaxillaB2csInvoiceResponse extends BaseResponse {
	private List<B2csInvoiceVo> data;

	public List<B2csInvoiceVo> getData() {
		return data;
	}

	public void setData(List<B2csInvoiceVo> data) {
		this.data = data;
	}
}
