package com.blackstrawai.response.keycontact.vendor;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.keycontact.vendor.VendorVo;

public class VendorResponse extends BaseResponse {

	private VendorVo data;

	public VendorVo getData() {
		return data;
	}

	public void setData(VendorVo data) {
		this.data = data;
	}

}
