package com.blackstrawai.response.externalintegration.general;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.general.aadhaar.AadhaarPanAdvanceVo;

public class AadhaarPanAdvanceResponse extends BaseResponse{

	private AadhaarPanAdvanceVo data;

	public AadhaarPanAdvanceVo getData() {
		return data;
	}

	public void setData(AadhaarPanAdvanceVo data) {
		this.data = data;
	}
	
	
}
