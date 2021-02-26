package com.blackstrawai.response.externalintegration.banking.perfios;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.banking.common.BasicAccountDetailsVo;

public class PerfiosListAccountResponse extends BaseResponse {
	private List<BasicAccountDetailsVo> data;

	public List<BasicAccountDetailsVo> getData() {
		return data;
	}

	public void setData(List<BasicAccountDetailsVo> data) {
		this.data = data;
	}
}
