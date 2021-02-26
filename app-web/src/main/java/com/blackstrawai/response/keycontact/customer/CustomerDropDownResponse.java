package com.blackstrawai.response.keycontact.customer;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.keycontact.dropdowns.CustomerDropdownVo;

public class
CustomerDropDownResponse extends BaseResponse{

	private CustomerDropdownVo data;

	public CustomerDropdownVo getData() {
		return data;
	}

	public void setData(CustomerDropdownVo data) {
		this.data = data;
	}
}
