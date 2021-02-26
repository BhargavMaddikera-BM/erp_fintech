package com.blackstrawai.response.settings;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.ProductCategoryVo;

public class ProductCategoryListResponse extends BaseResponse {


		private List<ProductCategoryVo> data;

		public List<ProductCategoryVo> getData() {
			return data;
		}

		public void setData(List<ProductCategoryVo> data) {
			this.data = data;
		}

		
}
