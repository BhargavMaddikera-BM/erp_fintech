package com.blackstrawai.response.ap.purchaseorder;

import java.util.List;

import com.blackstrawai.ap.purchaseorder.PoListVo;
import com.blackstrawai.common.BaseResponse;

public class ListPurchaseOrderResponse extends BaseResponse{
	private List<PoListVo>data;

	public List<PoListVo> getData() {
		return data;
	}

	public void setData(List<PoListVo> data) {
		this.data = data;
	}

}
