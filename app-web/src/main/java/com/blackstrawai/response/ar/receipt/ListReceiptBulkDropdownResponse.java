package com.blackstrawai.response.ar.receipt;

import java.util.List;

import com.blackstrawai.ar.dropdowns.ReceiptBulkDropdownVo;
import com.blackstrawai.common.BaseResponse;

public class ListReceiptBulkDropdownResponse extends BaseResponse{

	private List<ReceiptBulkDropdownVo> data;

	public List<ReceiptBulkDropdownVo> getData() {
		return data;
	}

	public void setData(List<ReceiptBulkDropdownVo> data) {
		this.data = data;
	}

	
	
}
