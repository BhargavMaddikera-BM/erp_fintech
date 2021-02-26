package com.blackstrawai.response.vendorsettings;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.vendorsettings.VamVendorGroupSettingsVo;

public class VamVendorGroupSettingsResponse extends BaseResponse {

	private VamVendorGroupSettingsVo data;

	public VamVendorGroupSettingsVo getData() {
		return data;
	}

	public void setData(VamVendorGroupSettingsVo data) {
		this.data = data;
	}

}
