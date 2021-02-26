package com.blackstrawai.payroll.dropdowns;

import java.util.List;

import com.blackstrawai.common.BaseVo;
import com.blackstrawai.payroll.BasicPayTypeVo;

public class PayTypeDropDownVo extends BaseVo{
	
	private List<BasicPayTypeVo>payType;

	public List<BasicPayTypeVo> getPayType() {
		return payType;
	}

	public void setPayType(List<BasicPayTypeVo> payType) {
		this.payType = payType;
	}

}
