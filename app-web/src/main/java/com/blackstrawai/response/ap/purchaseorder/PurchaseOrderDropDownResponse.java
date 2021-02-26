package com.blackstrawai.response.ap.purchaseorder;

import com.blackstrawai.ap.dropdowns.PurchaseOrderDropDownVo;
import com.blackstrawai.common.BaseResponse;

public class PurchaseOrderDropDownResponse extends BaseResponse{
	
	private PurchaseOrderDropDownVo data;

	public PurchaseOrderDropDownVo getData() {
		return data;
	}

	public void setData(PurchaseOrderDropDownVo data) {
		this.data = data;
	}

}
