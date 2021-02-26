package com.blackstrawai.response.banking.dashboard;

import com.blackstrawai.banking.dashboard.BankMasterCashAccountVo;
import com.blackstrawai.common.BaseResponse;

public class BankMasterCashAccountResponse extends BaseResponse {

	private BankMasterCashAccountVo data;

	public BankMasterCashAccountVo getData() {
		return data;
	}

	public void setData(BankMasterCashAccountVo data) {
		this.data = data;
	}

}
