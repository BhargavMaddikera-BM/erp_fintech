package com.blackstrawai.response.ap.billsinvoice;

import com.blackstrawai.ap.billsinvoice.InvoiceVo;
import com.blackstrawai.common.BaseResponse;

public class InvoiceResponse extends BaseResponse{

	private InvoiceVo data;

	public InvoiceVo getData() {
		return data;
	}

	public void setData(InvoiceVo data) {
		this.data = data;
	}

}
