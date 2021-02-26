package com.blackstrawai.response.ar.template;

import java.util.List;

import com.blackstrawai.ar.template.CreditNoteTemplateVo;
import com.blackstrawai.common.BaseResponse;

public class CreditNoteTemplateListResponse extends BaseResponse {
	
	private List<CreditNoteTemplateVo> creditNoteTemplate;

	public List<CreditNoteTemplateVo> getCreditNoteTemplate() {
		return creditNoteTemplate;
	}

	public void setCreditNoteTemplate(List<CreditNoteTemplateVo> creditNoteTemplate) {
		this.creditNoteTemplate = creditNoteTemplate;
	}
	
	

}
