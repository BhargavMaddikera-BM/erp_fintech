package com.blackstrawai.response.externalintegration.banking.yesbank;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.yesbank.YesBankCustomerInformationVo;

public class YesBankCustomerInformationResponse extends BaseResponse{
	 private YesBankCustomerInformationVo data;

	  public YesBankCustomerInformationVo getData() {
	    return data;
	  }

	  public void setData(YesBankCustomerInformationVo data) {
	    this.data = data;
	  }
}
