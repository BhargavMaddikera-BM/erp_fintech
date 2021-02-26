package com.blackstrawai.response.accounting;

import com.blackstrawai.accounting.AccountingAspectsVo;
import com.blackstrawai.common.BaseResponse;

public class AccountingAspectsResponse extends BaseResponse{

	private AccountingAspectsVo data;

	public AccountingAspectsVo getData() {
		return data;
	}

	public void setData(AccountingAspectsVo data) {
		this.data = data;
	}
	
	
}
