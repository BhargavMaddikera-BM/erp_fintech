package com.blackstrawai.response.ap.creditnote;


import com.blackstrawai.ap.creditnote.VendorCreditCreateVo;
import com.blackstrawai.common.BaseResponse;

public class VendorCreditResponse extends BaseResponse {

	private VendorCreditCreateVo data;

	public VendorCreditCreateVo getData() {
		return data;
	}

	public void setData(VendorCreditCreateVo data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "CreditNoteDropDownResponse [data=" + data + "]";
	}
}
