package com.blackstrawai.response.externalintegration.general;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.general.razorpay.IFSCCodeVo;

public class IFSCCodeResponse extends BaseResponse{

	private IFSCCodeVo data;

	public IFSCCodeVo getData() {
		return data;
	}

	public void setData(IFSCCodeVo data) {
		this.data = data;
	}
	
	
}
