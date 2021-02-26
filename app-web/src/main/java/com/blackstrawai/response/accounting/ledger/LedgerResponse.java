package com.blackstrawai.response.accounting.ledger;

import com.blackstrawai.accounting.ledger.LedgerVo;
import com.blackstrawai.common.BaseResponse;

public class LedgerResponse extends BaseResponse{


	private LedgerVo data;

	public LedgerVo getData() {
		return data;
	}

	public void setData(LedgerVo data) {
		this.data = data;
	}
}
