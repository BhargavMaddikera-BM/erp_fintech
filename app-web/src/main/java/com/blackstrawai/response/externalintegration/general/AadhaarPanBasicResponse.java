package com.blackstrawai.response.externalintegration.general;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.general.aadhaar.AadhaarPanBasicVo;

public class AadhaarPanBasicResponse extends BaseResponse{

	private AadhaarPanBasicVo data;

	public AadhaarPanBasicVo getData() {
		return data;
	}

	public void setData(AadhaarPanBasicVo data) {
		this.data = data;
	}
	
	
}
