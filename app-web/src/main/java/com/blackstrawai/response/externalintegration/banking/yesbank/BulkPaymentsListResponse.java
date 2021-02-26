package com.blackstrawai.response.externalintegration.banking.yesbank;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.yesbank.Request.bulkpayments.BulkPaymentsListVo;

public class BulkPaymentsListResponse extends BaseResponse{

	  private List<BulkPaymentsListVo> data;

	public List<BulkPaymentsListVo> getData() {
		return data;
	}

	public void setData(List<BulkPaymentsListVo> data) {
		this.data = data;
	}

}
