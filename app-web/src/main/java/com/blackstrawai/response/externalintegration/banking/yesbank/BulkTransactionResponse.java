package com.blackstrawai.response.externalintegration.banking.yesbank;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.yesbank.Response.bulkpayments.BulkTransactionVo;

public class BulkTransactionResponse extends BaseResponse{

	private BulkTransactionVo data;

	public BulkTransactionVo getData() {
		return data;
	}

	public void setData(BulkTransactionVo data) {
		this.data = data;
	}
	
	
}
