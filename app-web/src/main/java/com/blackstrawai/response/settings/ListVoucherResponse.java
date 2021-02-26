package com.blackstrawai.response.settings;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.VoucherVo;

public class ListVoucherResponse extends BaseResponse{

	private List<VoucherVo> data;

	public List<VoucherVo> getData() {
		return data;
	}

	public void setData(List<VoucherVo> data) {
		this.data = data;
	}
	
}
