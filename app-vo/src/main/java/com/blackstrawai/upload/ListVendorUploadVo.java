package com.blackstrawai.upload;

import java.util.List;

import com.blackstrawai.common.BaseVo;

public class ListVendorUploadVo extends BaseVo {

	private List<VendorUploadVo> vendor;

	public List<VendorUploadVo> getVendor() {
		return vendor;
	}

	public void setVendor(List<VendorUploadVo> vendor) {
		this.vendor = vendor;
	}

	}
