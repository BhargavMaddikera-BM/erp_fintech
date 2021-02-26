package com.blackstrawai.response.settings;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.PaymentTermsVo;

public class ListPaymentTermsResponse extends BaseResponse{

	private List<PaymentTermsVo> data;

	public List<PaymentTermsVo> getData() {
		return data;
	}

	public void setData(List<PaymentTermsVo> data) {
		this.data = data;
	}

	
	
}
