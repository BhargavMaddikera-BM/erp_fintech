package com.blackstrawai.response.ap.billsinvoice;

import com.blackstrawai.ap.dropdowns.InvoiceDropDownVo;
import com.blackstrawai.common.BaseResponse;

public class InvoicesDropDownResponse extends BaseResponse{

	private InvoiceDropDownVo data;

	public InvoiceDropDownVo getData() {
		return data;
	}

	public void setData(InvoiceDropDownVo data) {
		this.data = data;
	}
}
