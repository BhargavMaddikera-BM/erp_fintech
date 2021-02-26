package com.blackstrawai.response.ap.billsinvoice;

import java.util.List;

import com.blackstrawai.ap.billsinvoice.InvoiceListVo;
import com.blackstrawai.common.BaseResponse;

public class InvoiceListResponse extends BaseResponse{
	private List<InvoiceListVo>data;

	public List<InvoiceListVo> getData() {
		return data;
	}

	public void setData(List<InvoiceListVo> data) {
		this.data = data;
	}
}
