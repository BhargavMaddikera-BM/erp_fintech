package com.blackstrawai.response.externalintegration.banking.perfios;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.banking.perfios.PerfiosStatementInstitutionVo;

public class PerfiosStatementInstitutionResponse extends BaseResponse {
	private List<PerfiosStatementInstitutionVo> data;

	public List<PerfiosStatementInstitutionVo> getData() {
		return data;
	}

	public void setData(List<PerfiosStatementInstitutionVo> data) {
		this.data = data;
	}
}
