package com.blackstrawai.response.ap.payment.noncore;

import java.util.List;

import com.blackstrawai.ap.payment.noncore.ListPaymentNonCoreVo;
import com.blackstrawai.common.BaseResponse;

public class ListPaymentNonCoreResponse extends BaseResponse{
	private List<ListPaymentNonCoreVo> data;

	public List<ListPaymentNonCoreVo> getData() {
		return data;
	}

	public void setData(List<ListPaymentNonCoreVo> data) {
		this.data = data;
	}
}
