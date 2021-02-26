package com.blackstrawai.response.settings;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.VoucherVo;

public class VoucherResponse extends BaseResponse{

	private VoucherVo data;

	public VoucherVo getData() {
		return data;
	}

	public void setData(VoucherVo data) {
		this.data = data;
	}
	
}
