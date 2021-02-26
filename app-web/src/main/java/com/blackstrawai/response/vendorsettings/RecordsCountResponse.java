package com.blackstrawai.response.vendorsettings;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.vendorsettings.RecordsSubmittedVo;

public class RecordsCountResponse extends BaseResponse {

	private List<RecordsSubmittedVo> data;

	public List<RecordsSubmittedVo> getData() {
		return data;
	}

	public void setData(List<RecordsSubmittedVo> data) {
		this.data = data;
	}

}
