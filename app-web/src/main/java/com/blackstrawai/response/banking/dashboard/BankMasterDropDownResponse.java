package com.blackstrawai.response.banking.dashboard;

import com.blackstrawai.banking.dropdowns.BankMasterDropDownVo;
import com.blackstrawai.common.BaseResponse;

public class BankMasterDropDownResponse extends BaseResponse{

	private BankMasterDropDownVo data;

	public BankMasterDropDownVo getData() {
		return data;
	}

	public void setData(BankMasterDropDownVo data) {
		this.data = data;
	}
	
	
}
