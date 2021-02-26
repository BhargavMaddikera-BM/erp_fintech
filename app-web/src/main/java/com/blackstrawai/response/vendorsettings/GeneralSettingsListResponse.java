package com.blackstrawai.response.vendorsettings;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.vendorsettings.GeneralSettingsVo;

public class GeneralSettingsListResponse extends BaseResponse{

	private List<GeneralSettingsVo> data;

	public List<GeneralSettingsVo> getData() {
		return data;
	}

	public void setData(List<GeneralSettingsVo> data) {
		this.data = data;
	}
	
}
