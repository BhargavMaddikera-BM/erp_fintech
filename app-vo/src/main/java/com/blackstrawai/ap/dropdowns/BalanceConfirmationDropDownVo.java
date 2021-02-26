package com.blackstrawai.ap.dropdowns;

import java.util.List;

import com.blackstrawai.ap.balanceconfirmation.VendorAddressVo;

public class BalanceConfirmationDropDownVo {

	private VendorAddressVo companyDetails;
	private List<BasicCurrencyVo> currency;

	public VendorAddressVo getCompanyDetails() {
		return companyDetails;
	}

	public void setCompanyDetails(VendorAddressVo companyDetails) {
		this.companyDetails = companyDetails;
	}

	public List<BasicCurrencyVo> getCurrency() {
		return currency;
	}

	public void setCurrency(List<BasicCurrencyVo> currency) {
		this.currency = currency;
	}

}
