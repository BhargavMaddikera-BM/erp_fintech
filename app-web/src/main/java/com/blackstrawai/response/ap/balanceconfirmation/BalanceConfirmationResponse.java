package com.blackstrawai.response.ap.balanceconfirmation;

import com.blackstrawai.ap.balanceconfirmation.BalanceConfirmationVo;
import com.blackstrawai.common.BaseResponse;

public class BalanceConfirmationResponse extends BaseResponse {

	private BalanceConfirmationVo data;

	public BalanceConfirmationVo getData() {
		return data;
	}

	public void setData(BalanceConfirmationVo data) {
		this.data = data;
	}

}
