package com.blackstrawai.response.keycontact.customer;

import java.util.List;

import com.blackstrawai.common.BaseResponse;

public class ListCustomerResponse extends BaseResponse{
		private List<CustomerBriefResponse>data;

		public List<CustomerBriefResponse> getData() {
			return data;
		}

		public void setData(List<CustomerBriefResponse> data) {
			this.data = data;
		}

}
