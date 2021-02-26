package com.blackstrawai.response.ap.vendorportal.purchaseorder;

import java.util.List;

import com.blackstrawai.ap.vendorportal.purchaseorder.VendorPortalPoListVo;
import com.blackstrawai.common.BaseResponse;

public class VpPurchaseOrderListResponse extends BaseResponse{
	private List<VendorPortalPoListVo>data;

	public List<VendorPortalPoListVo> getData() {
		return data;
	}

	public void setData(List<VendorPortalPoListVo> data) {
		this.data = data;
	}

}
