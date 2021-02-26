package com.blackstrawai.response.ar.lut;

import java.util.List;

import com.blackstrawai.ar.lut.LetterOfUndertakingVo;
import com.blackstrawai.common.BaseResponse;

public class ListLetterOfUndertakingResponse extends BaseResponse{

	private List<LetterOfUndertakingVo> data;

	public List<LetterOfUndertakingVo> getData() {
		return data;
	}

	public void setData(List<LetterOfUndertakingVo> data) {
		this.data = data;
	}

	
	
}
