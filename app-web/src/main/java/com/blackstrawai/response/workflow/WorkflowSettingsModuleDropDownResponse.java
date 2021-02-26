package com.blackstrawai.response.workflow;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.upload.dropdowns.ModuleTypeDropDownVo;

public class WorkflowSettingsModuleDropDownResponse extends BaseResponse{

	private ModuleTypeDropDownVo data;

	public ModuleTypeDropDownVo getData() {
		return data;
	}

	public void setData(ModuleTypeDropDownVo data) {
		this.data = data;
	}

	
	
}
