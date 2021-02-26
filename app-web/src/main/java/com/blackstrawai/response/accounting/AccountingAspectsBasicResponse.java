package com.blackstrawai.response.accounting;

import java.util.List;

import com.blackstrawai.accounting.AccountingAspectsBasicVo;
import com.blackstrawai.common.BaseResponse;

public class AccountingAspectsBasicResponse extends BaseResponse{

	private List<AccountingAspectsBasicVo> data;

	public List<AccountingAspectsBasicVo> getData() {
		return data;
	}

	public void setData(List<AccountingAspectsBasicVo> data) {
		this.data = data;
	}
	
	
}
