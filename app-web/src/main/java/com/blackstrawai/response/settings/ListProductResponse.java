
package com.blackstrawai.response.settings;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.ListBasicProductVo;

public class ListProductResponse extends BaseResponse {

	List<ListBasicProductVo> data;

	public List<ListBasicProductVo> getData() {
		return data;
	}

	public void setData(List<ListBasicProductVo> data) {
		this.data = data;
	}

}
