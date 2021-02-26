package com.blackstrawai.response.ap.template;

import java.util.List;

import com.blackstrawai.ap.template.PurchaseOrderTemplateInformationVo;
import com.blackstrawai.common.BaseResponse;

public class PurchaseOrderTemplateInfoListResponse extends BaseResponse {
	
	private List<PurchaseOrderTemplateInformationVo> PurchaseOrderTemplate;
	
	public List<PurchaseOrderTemplateInformationVo> getPurchaseOrderTemplate() {
		return PurchaseOrderTemplate;
	}

	public void setPurchaseOrderTemplate(List<PurchaseOrderTemplateInformationVo> purchaseOrderTemplate) {
		PurchaseOrderTemplate = purchaseOrderTemplate;
	}
}
