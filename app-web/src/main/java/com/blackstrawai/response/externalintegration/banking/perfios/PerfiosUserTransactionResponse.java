package com.blackstrawai.response.externalintegration.banking.perfios;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.banking.perfios.PerfiosCallBackProcessingVo;

public class PerfiosUserTransactionResponse extends BaseResponse {
	
	public PerfiosCallBackProcessingVo getData() {
		return data;
	}

	public void setData(PerfiosCallBackProcessingVo data) {
		this.data = data;
	}

	private PerfiosCallBackProcessingVo data;
	
		
}
