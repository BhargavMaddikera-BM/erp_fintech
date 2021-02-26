package com.blackstrawai.request.settings;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.ProductDropDownVo;

public class ProductDropDownResponse extends BaseResponse {
	private ProductDropDownVo data;

	public ProductDropDownVo getData() {
		return data;
	}

	public void setData(ProductDropDownVo data) {
		this.data = data;
	}

}
