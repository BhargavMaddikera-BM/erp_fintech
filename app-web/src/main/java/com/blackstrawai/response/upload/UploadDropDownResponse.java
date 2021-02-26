package com.blackstrawai.response.upload;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.upload.dropdowns.ModuleTypeDropDownVo;

public class UploadDropDownResponse extends BaseResponse {

	private ModuleTypeDropDownVo data;

	public ModuleTypeDropDownVo getData() {
		return data;
	}

	public void setData(ModuleTypeDropDownVo data) {
		this.data = data;
	}

}
