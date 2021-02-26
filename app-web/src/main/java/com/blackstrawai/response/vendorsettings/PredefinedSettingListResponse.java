package com.blackstrawai.response.vendorsettings;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.vendorsettings.PredefinedSettingsVo;

public class PredefinedSettingListResponse extends BaseResponse{
	private List<PredefinedSettingsVo>data;

	public List<PredefinedSettingsVo> getData() {
		return data;
	}

	public void setData(List<PredefinedSettingsVo> data) {
		this.data = data;
	}
}
