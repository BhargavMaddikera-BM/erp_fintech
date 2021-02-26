package com.blackstrawai.response.ap.purchaseorder;

import com.blackstrawai.ap.purchaseorder.PurchaseOrderVo;
import com.blackstrawai.common.BaseResponse;

public class PurchaseOrderResponse extends BaseResponse{

	
	private PurchaseOrderVo data;

	public PurchaseOrderVo getData() {
		return data;
	}

	public void setData(PurchaseOrderVo data) {
		this.data = data;
	}
	
}
