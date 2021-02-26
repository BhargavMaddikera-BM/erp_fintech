package com.blackstrawai.response.banking.dashboard;

import com.blackstrawai.banking.dashboard.BankMasterWalletVo;
import com.blackstrawai.common.BaseResponse;

public class BankMasterWalletResponse extends BaseResponse{

	private BankMasterWalletVo data;

	public BankMasterWalletVo getData() {
		return data;
	}

	public void setData(BankMasterWalletVo data) {
		this.data = data;
	}
	
}
