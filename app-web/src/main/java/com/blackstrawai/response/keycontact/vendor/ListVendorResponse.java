package com.blackstrawai.response.keycontact.vendor;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.keycontact.vendor.VendorVo;

public class ListVendorResponse extends BaseResponse {

	private List<VendorVo> data;

	public List<VendorVo> getData() {
		return data;
	}

	public void setData(List<VendorVo> data) {
		this.data = data;
	}

}
