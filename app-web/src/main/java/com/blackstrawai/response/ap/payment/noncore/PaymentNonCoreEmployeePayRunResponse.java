package com.blackstrawai.response.ap.payment.noncore;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.payroll.payrun.PayRunEmployeeAmountVo;

public class PaymentNonCoreEmployeePayRunResponse extends BaseResponse {
	private List<PayRunEmployeeAmountVo> data;

	public List<PayRunEmployeeAmountVo> getData() {
		return data;
	}

	public void setData(List<PayRunEmployeeAmountVo> data) {
		this.data = data;
	}
}
