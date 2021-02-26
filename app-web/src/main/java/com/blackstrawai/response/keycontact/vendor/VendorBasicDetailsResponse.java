package com.blackstrawai.response.keycontact.vendor;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.keycontact.vendor.VendorBasicDetailsVo;

public class VendorBasicDetailsResponse extends BaseResponse{

	private List<VendorBasicDetailsVo> data;

	public List<VendorBasicDetailsVo> getData() {
		return data;
	}

	public void setData(List<VendorBasicDetailsVo> data) {
		this.data = data;
	}

	
	
}
