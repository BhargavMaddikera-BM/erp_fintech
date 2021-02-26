package com.blackstrawai.externalintegration.compliance.employeeprovidentfund;

import java.util.List;

public class EmployeeProvidentFundRefreshVo {
	private List<EmployeeProvidentFundChallanVo> challan;
	private List<EmployeeProvidentFundEcrVo> ecr;
	public List<EmployeeProvidentFundChallanVo> getChallan() {
		return challan;
	}
	public void setChallan(List<EmployeeProvidentFundChallanVo> challan) {
		this.challan = challan;
	}
	public List<EmployeeProvidentFundEcrVo> getEcr() {
		return ecr;
	}
	public void setEcr(List<EmployeeProvidentFundEcrVo> ecr) {
		this.ecr = ecr;
	}
	
	
}
