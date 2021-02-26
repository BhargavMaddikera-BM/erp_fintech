package com.blackstrawai.response.externalintegration.banking.yesbank;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.yesbank.dropdowns.BeneficiaryDropDownVo;

public class BeneficiaryDropDownResponse extends BaseResponse{

	 private BeneficiaryDropDownVo data;

	  public BeneficiaryDropDownVo getData() {
	    return data;
	  }

	  public void setData(BeneficiaryDropDownVo data) {
	    this.data = data;
	  }
}
