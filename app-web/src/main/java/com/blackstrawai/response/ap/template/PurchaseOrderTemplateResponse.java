package com.blackstrawai.response.ap.template;


import com.blackstrawai.ap.template.PurchaseOrderTemplateVo;
import com.blackstrawai.common.BaseResponse;

public class PurchaseOrderTemplateResponse extends BaseResponse {
	
	private PurchaseOrderTemplateVo data;

	public PurchaseOrderTemplateVo getData() {
		return data;
	}

	public void setData(PurchaseOrderTemplateVo purchaseOrderTempVo) {
		this.data = purchaseOrderTempVo;
	}

}
