package com.blackstrawai.response.ar.invoice;

import com.blackstrawai.ar.invoice.ArInvoiceVo;
import com.blackstrawai.common.BaseResponse;

public class ArInvoiceResponse extends BaseResponse{

	private ArInvoiceVo data;

	public ArInvoiceVo getData() {
		return data;
	}

	public void setData(ArInvoiceVo data) {
		this.data = data;
	}

}
