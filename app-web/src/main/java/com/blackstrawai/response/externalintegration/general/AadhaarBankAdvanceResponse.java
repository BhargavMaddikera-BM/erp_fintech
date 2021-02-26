package com.blackstrawai.response.externalintegration.general;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.general.aadhaar.AadhaarBankAdvanceVo;

public class AadhaarBankAdvanceResponse extends BaseResponse{

	private AadhaarBankAdvanceVo data;

	public AadhaarBankAdvanceVo getData() {
		return data;
	}

	public void setData(AadhaarBankAdvanceVo data) {
		this.data = data;
	}
	
	
}
