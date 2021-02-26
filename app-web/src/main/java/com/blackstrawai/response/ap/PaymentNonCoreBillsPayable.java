package com.blackstrawai.response.ap;

import com.blackstrawai.ap.payment.noncore.BillsPayableVo;
import com.blackstrawai.common.BaseResponse;

public class PaymentNonCoreBillsPayable extends BaseResponse {
	private BillsPayableVo data;

	public BillsPayableVo getData() {
		return data;
	}

	public void setData(BillsPayableVo data) {
		this.data = data;
	}
	
}
