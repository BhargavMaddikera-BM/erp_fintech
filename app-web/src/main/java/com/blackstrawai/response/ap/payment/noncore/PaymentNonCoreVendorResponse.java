package com.blackstrawai.response.ap.payment.noncore;

import java.util.List;

import com.blackstrawai.ap.purchaseorder.PoReferenceNumberVo;
import com.blackstrawai.common.BaseResponse;

public class PaymentNonCoreVendorResponse extends BaseResponse{
	
	private List<PoReferenceNumberVo> data;

	public List<PoReferenceNumberVo> getData() {
		return data;
	}

	public void setData(List<PoReferenceNumberVo> data) {
		this.data = data;
	}



}
