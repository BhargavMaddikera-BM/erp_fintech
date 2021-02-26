package com.blackstrawai.settings;

import java.util.List;

public class TaxRateVariantsVo {
	private List<TaxRateMappingVo> intraTaxRates;
	private List<TaxRateMappingVo> interTaxRates;
	public List<TaxRateMappingVo> getIntraTaxRates() {
		return intraTaxRates;
	}
	public void setIntraTaxRates(List<TaxRateMappingVo> intraTaxRates) {
		this.intraTaxRates = intraTaxRates;
	}
	public List<TaxRateMappingVo> getInterTaxRates() {
		return interTaxRates;
	}
	public void setInterTaxRates(List<TaxRateMappingVo> interTaxRates) {
		this.interTaxRates = interTaxRates;
	}
	
}
