package com.blackstrawai.response.settings;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.ListPageCustomizationVo;

public class ListPageCustomizationResponse extends BaseResponse{

	private ListPageCustomizationVo data;

	public ListPageCustomizationVo getData() {
		return data;
	}

	public void setData(ListPageCustomizationVo data) {
		this.data = data;
	}

	
}
