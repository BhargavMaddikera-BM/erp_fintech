package com.blackstrawai.response.payroll.payrun;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.BaseVo;
import com.blackstrawai.payroll.payrun.PayRunImportVo;

public class PayRunImportResponse extends BaseResponse  {
	
	private BaseVo data;

	public BaseVo getData() {
		return data;
	}

	public void setData(BaseVo data) {
		this.data = data;
	}
	
	

}
