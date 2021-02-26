package com.blackstrawai.response.ap.balanceconfirmation;

import java.util.List;

import com.blackstrawai.ap.balanceconfirmation.BalanceConfirmationBasicVo;
import com.blackstrawai.common.BaseResponse;

public class BalanceConfirmationListResponse extends BaseResponse {

	private List<BalanceConfirmationBasicVo> data;

	public List<BalanceConfirmationBasicVo> getData() {
		return data;
	}

	public void setData(List<BalanceConfirmationBasicVo> data) {
		this.data = data;
	}

}
