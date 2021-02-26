package com.blackstrawai.response.externalintegration.general;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.general.aadhaar.AadhaarGstnBasicVo;

public class AadhaarGstnBasicResponse extends BaseResponse{

	private AadhaarGstnBasicVo data;

	public AadhaarGstnBasicVo getData() {
		return data;
	}

	public void setData(AadhaarGstnBasicVo data) {
		this.data = data;
	}
	
	
}
