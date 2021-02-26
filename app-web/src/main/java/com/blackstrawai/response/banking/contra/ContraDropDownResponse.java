package com.blackstrawai.response.banking.contra;

import com.blackstrawai.banking.dropdowns.ContraDropDownVo;
import com.blackstrawai.common.BaseResponse;

public class ContraDropDownResponse extends BaseResponse {

	private ContraDropDownVo data;

	public ContraDropDownVo getData() {
		return data;
	}

	public void setData(ContraDropDownVo data) {
		this.data = data;
	}

}
