package com.blackstrawai.response.settings;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.HsnCodeVo;

public class HsnCodeResponse extends BaseResponse{

	private List<HsnCodeVo> data;

	public List<HsnCodeVo> getData() {
		return data;
	}

	public void setData(List<HsnCodeVo> data) {
		this.data = data;
	}

	
}
