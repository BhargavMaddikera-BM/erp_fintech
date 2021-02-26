package com.blackstrawai.response.externalintegration.banking.perfios;

import java.util.List;
import java.util.Map;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.banking.perfios.PerfiosUserAccountTransactionVo;

public class PerfiosUniqueUserIdResponse extends BaseResponse {
	
	Map<String,List<PerfiosUserAccountTransactionVo>>data;

	public Map<String, List<PerfiosUserAccountTransactionVo>> getData() {
		return data;
	}

	public void setData(Map<String, List<PerfiosUserAccountTransactionVo>> data) {
		this.data = data;
	} 

}
