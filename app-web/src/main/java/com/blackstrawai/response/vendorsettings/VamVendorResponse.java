package com.blackstrawai.response.vendorsettings;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.vendorsettings.VamBasicVendorVo;

public class VamVendorResponse extends BaseResponse {

	private List<VamBasicVendorVo> data;

	public List<VamBasicVendorVo> getData() {
		return data;
	}

	public void setData(List<VamBasicVendorVo> data) {
		this.data = data;
	}

}
