package com.blackstrawai.response.banking.contra;

import com.blackstrawai.banking.contra.ContraVo;
import com.blackstrawai.common.BaseResponse;

public class ContraResponse extends BaseResponse {

	private ContraVo data;

	public ContraVo getData() {
		return data;
	}

	public void setData(ContraVo data) {
		this.data = data;
	}

}
