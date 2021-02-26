package com.blackstrawai.response.keycontact.statutorybody;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.keycontact.statutorybody.StatutoryBodyVo;

public class ListStatutoryBodyResponse extends BaseResponse{

	private List<StatutoryBodyVo> data;

	public List<StatutoryBodyVo> getData() {
		return data;
	}

	public void setData(List<StatutoryBodyVo> data) {
		this.data = data;
	}

	
	
}
