package com.blackstrawai.response.settings;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.PaymentTermsVo;

public class PaymentTermsResponse extends BaseResponse{
	
	private PaymentTermsVo data;

	public PaymentTermsVo getData() {
		return data;
	}

	public void setData(PaymentTermsVo data) {
		this.data = data;
	}
	

}
