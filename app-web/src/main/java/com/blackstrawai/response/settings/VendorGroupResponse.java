package com.blackstrawai.response.settings;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.VendorGroupVo;

public class VendorGroupResponse extends BaseResponse{

	private VendorGroupVo data;

	public VendorGroupVo getData() {
		return data;
	}

	public void setData(VendorGroupVo data) {
		this.data = data;
	}
	
}
