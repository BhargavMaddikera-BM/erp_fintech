package com.blackstrawai.response.vendorsettings;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.vendorsettings.dropdowns.VamDropDownVo;

public class VamDropDownResponse extends BaseResponse {

	private VamDropDownVo data;

	public VamDropDownVo getData() {
		return data;
	}

	public void setData(VamDropDownVo data) {
		this.data = data;
	}

}
