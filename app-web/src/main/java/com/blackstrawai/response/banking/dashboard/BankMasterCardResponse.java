package com.blackstrawai.response.banking.dashboard;

import com.blackstrawai.banking.dashboard.BankMasterCardVo;
import com.blackstrawai.common.BaseResponse;

public class BankMasterCardResponse extends BaseResponse{

	private BankMasterCardVo data;

	public BankMasterCardVo getData() {
		return data;
	}

	public void setData(BankMasterCardVo data) {
		this.data = data;
	}
	
	
}
