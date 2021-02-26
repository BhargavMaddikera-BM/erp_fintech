package com.blackstrawai.response.ar.receipt;

import java.util.List;

import com.blackstrawai.ar.receipt.ReceiptVo;
import com.blackstrawai.common.BaseResponse;

public class ListReceiptResponse extends BaseResponse{

	private List<ReceiptVo> data;

	public List<ReceiptVo> getData() {
		return data;
	}

	public void setData(List<ReceiptVo> data) {
		this.data = data;
	}

	
	
}
