package com.blackstrawai.response.settings;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.SaCodeVo;

public class SaCodeResponse extends BaseResponse{

	private List<SaCodeVo> data;

	public List<SaCodeVo> getData() {
		return data;
	}

	public void setData(List<SaCodeVo> data) {
		this.data = data;
	}

	
	
}
