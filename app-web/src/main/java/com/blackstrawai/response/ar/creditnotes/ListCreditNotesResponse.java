package com.blackstrawai.response.ar.creditnotes;

import java.util.List;

import com.blackstrawai.ar.creditnotes.CreditNotesVo;
import com.blackstrawai.common.BaseResponse;

public class ListCreditNotesResponse extends BaseResponse{

	private List<CreditNotesVo> data;

	public List<CreditNotesVo> getData() {
		return data;
	}

	public void setData(List<CreditNotesVo> data) {
		this.data = data;
	}

	
	
}
