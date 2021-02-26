package com.blackstrawai.response.externalintegration.banking.yesbank;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.yesbank.erpbulkpayments.BaseERPPaymentVo;

public class ERPPaymentResponse extends BaseResponse{

	private BaseERPPaymentVo data;

	public BaseERPPaymentVo getData() {
		return data;
	}

	public void setData(BaseERPPaymentVo data) {
		this.data = data;
	}
	
}
