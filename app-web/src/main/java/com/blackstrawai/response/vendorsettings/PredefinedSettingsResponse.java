package com.blackstrawai.response.vendorsettings;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.vendorsettings.PredefinedSettingsVo;

public class PredefinedSettingsResponse  extends BaseResponse{
	private PredefinedSettingsVo data;

	public PredefinedSettingsVo getData() {
		return data;
	}

	public void setData(PredefinedSettingsVo data) {
		this.data = data;
	}
}
