package com.blackstrawai.response.externalintegration.banking.yesbank;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.yesbank.dropdowns.APVendorBillsDropDownVo;

public class APVendorBillsDropDownResponse extends BaseResponse{
	 private List<APVendorBillsDropDownVo> data;

	public List<APVendorBillsDropDownVo> getData() {
		return data;
	}

	public void setData(List<APVendorBillsDropDownVo> data) {
		this.data = data;
	}
	 
	 
	 
}
