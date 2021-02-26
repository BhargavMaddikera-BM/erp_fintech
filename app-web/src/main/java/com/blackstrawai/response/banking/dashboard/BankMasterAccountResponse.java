package com.blackstrawai.response.banking.dashboard;

import com.blackstrawai.banking.dashboard.BankMasterAccountVo;
import com.blackstrawai.common.BaseResponse;

public class BankMasterAccountResponse extends BaseResponse{

	private BankMasterAccountVo data;

	public BankMasterAccountVo getData() {
		return data;
	}

	public void setData(BankMasterAccountVo data) {
		this.data = data;
	}
	
}
