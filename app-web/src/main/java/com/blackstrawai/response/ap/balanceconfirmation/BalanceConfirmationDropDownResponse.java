package com.blackstrawai.response.ap.balanceconfirmation;

import com.blackstrawai.ap.dropdowns.BalanceConfirmationDropDownVo;
import com.blackstrawai.common.BaseResponse;

public class BalanceConfirmationDropDownResponse extends BaseResponse{

	private BalanceConfirmationDropDownVo data;

	public BalanceConfirmationDropDownVo getData() {
		return data;
	}

	public void setData(BalanceConfirmationDropDownVo data) {
		this.data = data;
	}

}
