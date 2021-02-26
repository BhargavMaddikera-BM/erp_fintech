package com.blackstrawai.response.accounting;

import com.blackstrawai.accounting.dropdowns.AccountingAspectsDropDownVo;
import com.blackstrawai.common.BaseResponse;

public class AccountingAspectsDropDownResponse extends BaseResponse{

	private AccountingAspectsDropDownVo data;

	public AccountingAspectsDropDownVo getData() {
		return data;
	}

	public void setData(AccountingAspectsDropDownVo data) {
		this.data = data;
	}
	
	
}
