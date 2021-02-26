package com.blackstrawai.response.vendorsettings;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.vendorsettings.VamVendorSettingsVo;

public class VamVendorSettingsResponse extends BaseResponse {

	private VamVendorSettingsVo data;

	public VamVendorSettingsVo getData() {
		return data;
	}

	public void setData(VamVendorSettingsVo data) {
		this.data = data;
	}

}
