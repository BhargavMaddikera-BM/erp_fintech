package com.blackstrawai.response.ar.template;


import com.blackstrawai.ar.template.InvoiceTemplateVo;
import com.blackstrawai.common.BaseResponse;

public class InvoiceTemplateResponse extends BaseResponse {
	
	private InvoiceTemplateVo data;

	public InvoiceTemplateVo getData() {
		return data;
	}

	public void setData(InvoiceTemplateVo data) {
		this.data = data;
	}

}
