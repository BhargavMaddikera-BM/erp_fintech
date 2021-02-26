package com.blackstrawai.response.externalintegration.banking.perfios;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.banking.perfios.PerfiosWidgetUrlVo;

public class PerfiosResponse extends BaseResponse {
	private PerfiosWidgetUrlVo data;

	public PerfiosWidgetUrlVo getData() {
		return data;
	}

	public void setData(PerfiosWidgetUrlVo data) {
		this.data = data;
	}
}
