package com.blackstrawai.response.ap;

import java.util.List;

import com.blackstrawai.ap.payment.noncore.MultiLevelInvoiceVo;
import com.blackstrawai.common.BaseResponse;

public class PaymentNonCoreOnAccountAdvancePaymentResponse extends BaseResponse {
	private List<MultiLevelInvoiceVo> data;

	public List<MultiLevelInvoiceVo> getData() {
		return data;
	}

	public void setData(List<MultiLevelInvoiceVo> data) {
		this.data = data;
	}
}
