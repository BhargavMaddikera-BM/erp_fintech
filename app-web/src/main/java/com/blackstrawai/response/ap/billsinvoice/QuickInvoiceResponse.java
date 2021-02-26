package com.blackstrawai.response.ap.billsinvoice;

import com.blackstrawai.ap.billsinvoice.QuickInvoiceVo;
import com.blackstrawai.common.BaseResponse;

public class QuickInvoiceResponse extends BaseResponse{

	private QuickInvoiceVo data;

	public QuickInvoiceVo getData() {
		return data;
	}

	public void setData(QuickInvoiceVo data) {
		this.data = data;
	}

}
