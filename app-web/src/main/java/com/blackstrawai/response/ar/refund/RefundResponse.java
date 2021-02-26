package com.blackstrawai.response.ar.refund;

import com.blackstrawai.ar.refund.RefundVo;
import com.blackstrawai.common.BaseResponse;

public class RefundResponse extends BaseResponse{

	private RefundVo data;

	public RefundVo getData() {
		return data;
	}

	public void setData(RefundVo data) {
		this.data = data;
	}

	
	
}
