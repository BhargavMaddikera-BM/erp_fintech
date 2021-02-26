package com.blackstrawai.response.externalintegration.general;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.general.aadhaar.AadhaarGstnAdvanceVo;

public class AadhaarGstnAdvanceResponse extends BaseResponse{

	private AadhaarGstnAdvanceVo data;

	public AadhaarGstnAdvanceVo getData() {
		return data;
	}

	public void setData(AadhaarGstnAdvanceVo data) {
		this.data = data;
	}
	
	
}
