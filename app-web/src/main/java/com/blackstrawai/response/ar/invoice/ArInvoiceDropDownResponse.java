package com.blackstrawai.response.ar.invoice;

import com.blackstrawai.ar.dropdowns.ArInvoicesDropDownVo;
import com.blackstrawai.common.BaseResponse;

public class ArInvoiceDropDownResponse extends BaseResponse{

	private ArInvoicesDropDownVo data;

	public ArInvoicesDropDownVo getData() {
		return data;
	}

	public void setData(ArInvoicesDropDownVo data) {
		this.data = data;
	}
}
