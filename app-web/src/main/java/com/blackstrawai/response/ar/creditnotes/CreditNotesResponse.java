package com.blackstrawai.response.ar.creditnotes;


import com.blackstrawai.ar.creditnotes.CreditNotesVo;
import com.blackstrawai.common.BaseResponse;

public class CreditNotesResponse extends BaseResponse{
	
	private CreditNotesVo data;

	public CreditNotesVo getData() {
		return data;
	}

	public void setData(CreditNotesVo data) {
		this.data = data;
	}
	

}
