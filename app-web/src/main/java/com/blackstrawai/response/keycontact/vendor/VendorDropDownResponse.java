package com.blackstrawai.response.keycontact.vendor;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.keycontact.dropdowns.VendorDropDownVo;

public class VendorDropDownResponse extends BaseResponse{
	
	private VendorDropDownVo data;

	public VendorDropDownVo getData() {
		return data;
	}

	public void setData(VendorDropDownVo data) {
		this.data = data;
	}


}
