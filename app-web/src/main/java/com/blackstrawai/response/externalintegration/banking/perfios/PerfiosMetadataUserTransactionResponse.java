package com.blackstrawai.response.externalintegration.banking.perfios;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.banking.perfios.PerfiosUserAccountTransactionVo;

public class PerfiosMetadataUserTransactionResponse extends BaseResponse {
	private boolean isUserExist;
	private List<PerfiosUserAccountTransactionVo> data;
	public boolean isUserExist() {
		return isUserExist;
	}
	public void setUserExist(boolean isUserExist) {
		this.isUserExist = isUserExist;
	}
	public List<PerfiosUserAccountTransactionVo> getData() {
		return data;
	}
	public void setData(List<PerfiosUserAccountTransactionVo> data) {
		this.data = data;
	}
	
	
}
