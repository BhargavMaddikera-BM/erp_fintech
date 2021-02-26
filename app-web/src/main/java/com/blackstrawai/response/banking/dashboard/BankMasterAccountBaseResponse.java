package com.blackstrawai.response.banking.dashboard;

import java.util.List;

import com.blackstrawai.banking.dashboard.BankMasterAccountBaseVo;
import com.blackstrawai.common.BaseResponse;

public class BankMasterAccountBaseResponse extends BaseResponse {

	private List<BankMasterAccountBaseVo> data;

	public List<BankMasterAccountBaseVo> getData() {
		return data;
	}

	public void setData(List<BankMasterAccountBaseVo> data) {
		this.data = data;
	}

}
