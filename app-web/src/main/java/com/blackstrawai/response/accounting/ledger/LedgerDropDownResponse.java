package com.blackstrawai.response.accounting.ledger;

import com.blackstrawai.accounting.dropdowns.LedgerDropDownVo;
import com.blackstrawai.common.BaseResponse;

public class LedgerDropDownResponse extends BaseResponse{

	private LedgerDropDownVo data;

	public LedgerDropDownVo getData() {
		return data;
	}

	public void setData(LedgerDropDownVo data) {
		this.data = data;
	}
}
