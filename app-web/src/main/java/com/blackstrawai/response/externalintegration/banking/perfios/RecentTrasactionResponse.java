package com.blackstrawai.response.externalintegration.banking.perfios;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.banking.perfios.RecentTransactionVo;

public class RecentTrasactionResponse extends BaseResponse {
	private List<RecentTransactionVo> data;

	public List<RecentTransactionVo> getData() {
		return data;
	}

	public void setData(List<RecentTransactionVo> data) {
		this.data = data;
	}
}
