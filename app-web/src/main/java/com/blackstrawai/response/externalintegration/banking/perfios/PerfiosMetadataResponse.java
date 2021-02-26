package com.blackstrawai.response.externalintegration.banking.perfios;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.banking.perfios.PerfiosMetadataVo;

public class PerfiosMetadataResponse extends BaseResponse {
	private List<PerfiosMetadataVo> data;

	public List<PerfiosMetadataVo> getData() {
		return data;
	}

	public void setData(List<PerfiosMetadataVo> data) {
		this.data = data;
	}
}
