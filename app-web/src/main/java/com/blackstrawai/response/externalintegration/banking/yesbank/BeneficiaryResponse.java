package com.blackstrawai.response.externalintegration.banking.yesbank;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.yesbank.beneficiary.YesBankBeneficiaryVo;

public class BeneficiaryResponse extends BaseResponse{
	 private YesBankBeneficiaryVo data;

	  public YesBankBeneficiaryVo getData() {
	    return data;
	  }

	  public void setData(YesBankBeneficiaryVo data) {
	    this.data = data;
	  }
}
