package com.blackstrawai.response.journals;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.journals.GeneralLedgerVo;

public class GeneralLedgerResponse extends BaseResponse {

	private GeneralLedgerVo data;

	public GeneralLedgerVo getData() {
		return data;
	}

	public void setData(GeneralLedgerVo data) {
		this.data = data;
	}
}
