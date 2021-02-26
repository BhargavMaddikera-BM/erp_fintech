package com.blackstrawai.response.ap.creditnote;

import com.blackstrawai.ap.dropdowns.VendorCreditNoteDropDownVo;
import com.blackstrawai.common.BaseResponse;

public class CreditNoteDropDownResponse extends BaseResponse {

	private VendorCreditNoteDropDownVo data;

	public VendorCreditNoteDropDownVo getData() {
		return data;
	}

	public void setData(VendorCreditNoteDropDownVo data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "CreditNoteDropDownResponse [data=" + data + "]";
	}

}
