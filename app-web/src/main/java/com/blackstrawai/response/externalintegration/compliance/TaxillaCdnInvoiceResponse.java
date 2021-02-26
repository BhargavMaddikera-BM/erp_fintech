package com.blackstrawai.response.externalintegration.compliance;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.compliance.taxilla.CdnInvoiceVo;

public class TaxillaCdnInvoiceResponse extends BaseResponse {
	private List<CdnInvoiceVo> data;

	public List<CdnInvoiceVo> getData() {
		return data;
	}

	public void setData(List<CdnInvoiceVo> data) {
		this.data = data;
	}
}
