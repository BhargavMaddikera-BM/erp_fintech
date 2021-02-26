package com.blackstrawai.response.settings;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.CustomerGroupVo;

public class ListCustomerGroupResponse extends BaseResponse{

	List<CustomerGroupVo> data;

	public List<CustomerGroupVo> getData() {
		return data;
	}

	public void setData(List<CustomerGroupVo> data) {
		this.data = data;
	}

	
}
