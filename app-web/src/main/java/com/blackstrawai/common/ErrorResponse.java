package com.blackstrawai.common;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.ErrorVo;


public class ErrorResponse extends BaseResponse {
	private ErrorVo data;

	public ErrorVo getData() {
		return data;
	}

	public void setData(ErrorVo data) {
		this.data = data;
	}

}
