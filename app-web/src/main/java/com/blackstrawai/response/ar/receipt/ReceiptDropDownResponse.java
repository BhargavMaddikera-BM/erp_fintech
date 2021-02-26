package com.blackstrawai.response.ar.receipt;

import com.blackstrawai.ar.dropdowns.ReceiptDropdownVo;
import com.blackstrawai.common.BaseResponse;

public class ReceiptDropDownResponse extends BaseResponse{

	private ReceiptDropdownVo data;

	public ReceiptDropdownVo getData() {
		return data;
	}

	public void setData(ReceiptDropdownVo data) {
		this.data = data;
	}

	
	
}
