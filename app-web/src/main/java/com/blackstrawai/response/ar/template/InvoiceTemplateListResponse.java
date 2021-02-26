package com.blackstrawai.response.ar.template;

import java.util.List;

import com.blackstrawai.ar.template.InvoiceTemplateVo;
import com.blackstrawai.common.BaseResponse;

public class InvoiceTemplateListResponse extends BaseResponse {
	
	private List<InvoiceTemplateVo> invoiceTemplate;

	public List<InvoiceTemplateVo> getInvoiceTemplate() {
		return invoiceTemplate;
	}

	public void setInvoiceTemplate(List<InvoiceTemplateVo> invoiceTemplate) {
		this.invoiceTemplate = invoiceTemplate;
	}
	
	

}
