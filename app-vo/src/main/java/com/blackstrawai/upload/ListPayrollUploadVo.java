package com.blackstrawai.upload;

import java.util.List;

import com.blackstrawai.common.BaseVo;

public class ListPayrollUploadVo extends BaseVo{
	
	private List<PayrollUploadVo> payRunInformation;

	public List<PayrollUploadVo> getPayRunInformation() {
		return payRunInformation;
	}

	public void setPayRunInformation(List<PayrollUploadVo> payroll) {
		this.payRunInformation = payroll;
	}

}
