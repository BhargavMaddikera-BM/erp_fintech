package com.blackstrawai.response.accounting.ledger;

import java.util.List;

import com.blackstrawai.accounting.ledger.LedgerListVo;
import com.blackstrawai.common.BaseResponse;

public class LedgerListResponse extends BaseResponse{

	private List<LedgerListVo> data;

	public List<LedgerListVo> getData() {
		return data;
	}

	public void setData(List<LedgerListVo> data) {
		this.data = data;
	}
}
