package com.blackstrawai.response.vendorsettings;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.vendorsettings.VamBasicVendorGroupVo;

public class VamVendorGroupResponse extends BaseResponse {

	private List<VamBasicVendorGroupVo> data;

	public List<VamBasicVendorGroupVo> getData() {
		return data;
	}

	public void setData(List<VamBasicVendorGroupVo> data) {
		this.data = data;
	}

}
