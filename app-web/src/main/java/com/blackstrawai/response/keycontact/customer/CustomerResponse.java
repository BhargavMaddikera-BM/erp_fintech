package com.blackstrawai.response.keycontact.customer;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.keycontact.customer.CustomerVo;

public class CustomerResponse extends BaseResponse{

		
		private CustomerVo data;

		public CustomerVo getData() {
			return data;
		}

		public void setData(CustomerVo data) {
			this.data = data;
		}

}
