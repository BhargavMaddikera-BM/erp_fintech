package com.blackstrawai.response.externalintegration.banking.yesbank;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.externalintegration.yesbank.beneficiary.BeneficiaryListVo;

public class BeneficiaryListResponse extends BaseResponse{

	private List<BeneficiaryListVo> data;

	  public List<BeneficiaryListVo> getData() {
	    return data;
	  }

	  public void setData(List<BeneficiaryListVo> data) {
	    this.data = data;
	  }
	  
}
