package com.blackstrawai.response.upload;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.BaseVo;

public class ImportFileResponse extends BaseResponse {

	private BaseVo data;

	public BaseVo getData() {
		return data;
	}

	public void setData(BaseVo data) {
		this.data = data;
	}

}
