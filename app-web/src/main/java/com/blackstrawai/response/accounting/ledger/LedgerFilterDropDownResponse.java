package com.blackstrawai.response.accounting.ledger;

import java.util.List;

import com.blackstrawai.accounting.dropdowns.LedgerFilterDropDownVo;
import com.blackstrawai.common.BaseResponse;

public class LedgerFilterDropDownResponse extends BaseResponse{
	private List<LedgerFilterDropDownVo> data;

	public List<LedgerFilterDropDownVo> getData() {
		return data;
	}

	public void setData(List<LedgerFilterDropDownVo> data) {
		this.data = data;
	}
}
