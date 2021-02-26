package com.blackstrawai.response.externalintegration.general;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.general.aadhaar.AadhaarBankBasicVo;

public class AadhaarBankBasicResponse extends BaseResponse{

	private AadhaarBankBasicVo data;

	public AadhaarBankBasicVo getData() {
		return data;
	}

	public void setData(AadhaarBankBasicVo data) {
		this.data = data;
	}
	
	
}
