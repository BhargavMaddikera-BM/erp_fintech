package com.blackstrawai.response.externalintegration.banking.yesbank;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.keycontact.vendor.VendorBankDetailsVo;

public class VendorBankResponse extends BaseResponse{
	 private VendorBankDetailsVo data;

	  public VendorBankDetailsVo getData() {
	    return data;
	  }

	  public void setData(VendorBankDetailsVo data) {
	    this.data = data;
	  }
}
