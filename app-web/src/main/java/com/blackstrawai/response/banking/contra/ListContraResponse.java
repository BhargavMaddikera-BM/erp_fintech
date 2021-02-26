package com.blackstrawai.response.banking.contra;

import java.util.List;

import com.blackstrawai.banking.contra.ContraBaseVo;
import com.blackstrawai.common.BaseResponse;

public class ListContraResponse extends BaseResponse {

	private List<ContraBaseVo> data;

	public List<ContraBaseVo> getData() {
		return data;
	}

	public void setData(List<ContraBaseVo> data) {
		this.data = data;
	}

}
