package com.blackstrawai.ap.dropdowns;

import java.util.List;

import com.blackstrawai.keycontact.vendor.VendorBasedOnGstVo;

public class BasicVendorDetailsVo extends BasicVendorVo{

	private String gstNumber;
	private Integer currencyId;
	private List<VendorBasedOnGstVo> gstAndAddress;
	
	
	

	public List<VendorBasedOnGstVo> getGstAndAddress() {
		return gstAndAddress;
	}

	public void setGstAndAddress(List<VendorBasedOnGstVo> gstAndAddress) {
		this.gstAndAddress = gstAndAddress;
	}

	public String getGstNumber() {
		return gstNumber;
	}

	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}
	
	
}
