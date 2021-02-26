package com.blackstrawai.response.ap;

import java.util.List;

import com.blackstrawai.ap.dropdowns.PaymentTypeVo;
import com.blackstrawai.common.BaseResponse;

public class PaymentNonCoreTypeResponse extends BaseResponse {
	private List<PaymentTypeVo> data;

	public List<PaymentTypeVo> getData() {
		return data;
	}

	public void setData(List<PaymentTypeVo> data) {
		this.data = data;
	}
}
