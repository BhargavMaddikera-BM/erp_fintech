package com.blackstrawai.response.payroll;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.payroll.PayTypeVo;

public class ListPayTypeResponse extends BaseResponse{
	
	private List<PayTypeVo>data;

	public List<PayTypeVo> getData() {
		return data;
	}

	public void setData(List<PayTypeVo> data) {
		this.data = data;
	}

	

}
