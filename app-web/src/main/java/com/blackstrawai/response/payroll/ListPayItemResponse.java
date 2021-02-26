package com.blackstrawai.response.payroll;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.payroll.PayItemVo;

public class ListPayItemResponse extends BaseResponse{
	
	private List<PayItemVo>data;

	public List<PayItemVo> getData() {
		return data;
	}

	public void setData(List<PayItemVo> data) {
		this.data = data;
	}

}
