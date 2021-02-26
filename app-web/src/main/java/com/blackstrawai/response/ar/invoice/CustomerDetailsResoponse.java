package com.blackstrawai.response.ar.invoice;

import com.blackstrawai.ar.dropdowns.BasicCustomerDetailsVo;
import com.blackstrawai.common.BaseResponse;

public class CustomerDetailsResoponse extends BaseResponse {

	private BasicCustomerDetailsVo data;

	public BasicCustomerDetailsVo getData() {
		return data;
	}

	public void setData(BasicCustomerDetailsVo data) {
		this.data = data;
	}
}
