package com.blackstrawai.response.ar.receipt;

import com.blackstrawai.ar.receipt.ReceiptVo;
import com.blackstrawai.common.BaseResponse;

public class ReceiptResponse extends BaseResponse{
	
	private ReceiptVo data;

	public ReceiptVo getData() {
		return data;
	}

	public void setData(ReceiptVo data) {
		this.data = data;
	}
	

}
