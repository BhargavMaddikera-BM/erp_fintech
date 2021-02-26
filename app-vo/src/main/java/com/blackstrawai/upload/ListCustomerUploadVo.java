package com.blackstrawai.upload;

import java.util.List;

import com.blackstrawai.common.BaseVo;

public class ListCustomerUploadVo extends BaseVo {

	private List<CustomerUploadVo> customer;

	public List<CustomerUploadVo> getCustomer() {
		return customer;
	}

	public void setCustomer(List<CustomerUploadVo> customer) {
		this.customer = customer;
	}

}
