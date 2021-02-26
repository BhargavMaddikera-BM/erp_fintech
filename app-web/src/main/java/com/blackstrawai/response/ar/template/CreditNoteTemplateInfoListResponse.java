package com.blackstrawai.response.ar.template;

import java.util.List;

import com.blackstrawai.ar.template.CreditNoteTemplateinformationVo;
import com.blackstrawai.common.BaseResponse;

public class CreditNoteTemplateInfoListResponse extends BaseResponse {
	
	private List<CreditNoteTemplateinformationVo> creditNoteTemplateInfo;

	public List<CreditNoteTemplateinformationVo> getCreditNoteTemplateInfo() {
		return creditNoteTemplateInfo;
	}

	public void setCreditNoteTemplateInfo(List<CreditNoteTemplateinformationVo> creditNoteTemplateInfo) {
		this.creditNoteTemplateInfo = creditNoteTemplateInfo;
	}
	
	

}
