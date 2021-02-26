package com.blackstrawai.response.ar.creditnotes;

import com.blackstrawai.ar.dropdowns.CustomerAccountsDropdownVo;
import com.blackstrawai.common.BaseResponse;

public class CustomerAccountsDropdownResponse extends BaseResponse{

	private CustomerAccountsDropdownVo data;

	public CustomerAccountsDropdownVo getData() {
		return data;
	}

	public void setData(CustomerAccountsDropdownVo data) {
		this.data = data;
	}

	
	
}
