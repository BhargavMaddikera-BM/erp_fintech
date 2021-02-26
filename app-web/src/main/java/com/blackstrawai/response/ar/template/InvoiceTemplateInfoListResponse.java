package com.blackstrawai.response.ar.template;

import java.util.List;

import com.blackstrawai.ar.template.InvoiceTemplateinformationVo;
import com.blackstrawai.common.BaseResponse;

public class InvoiceTemplateInfoListResponse extends BaseResponse {
	
	private List<InvoiceTemplateinformationVo> invoiceTemplate;
	
	public List<InvoiceTemplateinformationVo> getInvoiceTemplate() {
		return invoiceTemplate;
	}

	public void setInvoiceTemplate(List<InvoiceTemplateinformationVo> invoice) {
		this.invoiceTemplate = invoice;
	}

	
	
	

}
