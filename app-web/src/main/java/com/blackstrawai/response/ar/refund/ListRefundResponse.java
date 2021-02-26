package com.blackstrawai.response.ar.refund;

import java.util.List;

import com.blackstrawai.ar.refund.RefundVo;
import com.blackstrawai.common.BaseResponse;

public class ListRefundResponse extends BaseResponse{

	private List<RefundVo> data;

	public List<RefundVo> getData() {
		return data;
	}

	public void setData(List<RefundVo> data) {
		this.data = data;
	}

	
	
}
