
package com.blackstrawai.response.settings;


import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.ProductVo;

public class ProductResponse extends BaseResponse {

	ProductVo data;

	public ProductVo getData() {
		return data;
	}

	public void setData(ProductVo data) {
		this.data = data;
	}

}
