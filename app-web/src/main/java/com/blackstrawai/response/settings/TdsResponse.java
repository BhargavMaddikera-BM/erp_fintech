package com.blackstrawai.response.settings;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.settings.PaymentTermsVo;
import com.blackstrawai.settings.TdsVo;

public class TdsResponse extends BaseResponse{

		private TdsVo data;

		public TdsVo getData() {
			return data;
		}

		public void setData(TdsVo data) {
			this.data = data;
		}
		

}
