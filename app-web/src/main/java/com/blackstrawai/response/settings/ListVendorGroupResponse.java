package com.blackstrawai.response.settings;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.VendorGroupVo;

public class ListVendorGroupResponse extends BaseResponse{

	private List<VendorGroupVo> data;

	public List<VendorGroupVo> getData() {
		return data;
	}

	public void setData(List<VendorGroupVo> data) {
		this.data = data;
	}
	
}
