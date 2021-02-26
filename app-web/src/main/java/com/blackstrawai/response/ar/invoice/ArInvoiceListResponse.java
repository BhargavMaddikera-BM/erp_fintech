package com.blackstrawai.response.ar.invoice;

import java.util.List;

import com.blackstrawai.ar.invoice.ArInvoiceListVo;
import com.blackstrawai.common.BaseResponse;

public class ArInvoiceListResponse extends BaseResponse{
	private List<ArInvoiceListVo>data;

	public List<ArInvoiceListVo> getData() {
		return data;
	}

	public void setData(List<ArInvoiceListVo> data) {
		this.data = data;
	}
}
