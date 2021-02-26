package com.blackstrawai.response.settings;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.TdsVo;

public class ListTdsResponse extends BaseResponse{
	
	private List<TdsVo> data;

	public List<TdsVo> getData() {
		return data;
	}

	public void setData(List<TdsVo> data) {
		this.data = data;
	}


}
