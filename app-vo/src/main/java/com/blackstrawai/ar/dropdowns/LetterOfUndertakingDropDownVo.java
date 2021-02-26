package com.blackstrawai.ar.dropdowns;

import java.util.List;

import com.blackstrawai.ar.lut.FinYearExpiryDateVo;
import com.blackstrawai.common.TokenVo;
import com.blackstrawai.onboarding.organization.BasicGSTLocationDetailsVo;


public class LetterOfUndertakingDropDownVo extends TokenVo{
	private BasicGSTLocationDetailsVo locationGst;   
	private List<FinYearExpiryDateVo> financialYears;
	public BasicGSTLocationDetailsVo getLocationGst() {
		return locationGst;
	}
	public void setLocationGst(BasicGSTLocationDetailsVo locationGst) {
		this.locationGst = locationGst;
	}
	public List<FinYearExpiryDateVo> getFinancialYears() {
		return financialYears;
	}
	public void setFinancialYears(List<FinYearExpiryDateVo> financialYears) {
		this.financialYears = financialYears;
	}
	

}