package com.blackstrawai.response.settings;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.CustomerGroupVo;

public class CustomerGroupResponse extends BaseResponse{

	private CustomerGroupVo data;

	public CustomerGroupVo getData() {
		return data;
	}

	public void setData(CustomerGroupVo data) {
		this.data = data;
	}
	
	
}
