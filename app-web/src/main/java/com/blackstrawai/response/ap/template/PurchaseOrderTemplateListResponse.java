package com.blackstrawai.response.ap.template;

import java.util.List;

import com.blackstrawai.ap.template.PurchaseOrderTemplateVo;
import com.blackstrawai.common.BaseResponse;

public class PurchaseOrderTemplateListResponse extends BaseResponse {
	
	private List<PurchaseOrderTemplateVo> purchaseOrderTemplate;

	public List<PurchaseOrderTemplateVo> getPurchaseOrderTemplate() {
		return purchaseOrderTemplate;
	}

	public void setPurchaseOrderTemplate(List<PurchaseOrderTemplateVo> purchaseOrderTemplate) {
		this.purchaseOrderTemplate = purchaseOrderTemplate;
	}

		

}
