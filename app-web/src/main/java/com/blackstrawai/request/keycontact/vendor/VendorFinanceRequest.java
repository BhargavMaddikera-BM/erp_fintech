package com.blackstrawai.request.keycontact.vendor;

public class VendorFinanceRequest {

	private Integer id;
	private Integer currencyId;
	private Integer paymentTermsid;
	private Integer sourceOfSupplyId;
	private Integer tdsId;
	private String openingBalance;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}
	public Integer getPaymentTermsid() {
		return paymentTermsid;
	}
	public void setPaymentTermsid(Integer paymentTermsid) {
		this.paymentTermsid = paymentTermsid;
	}
	public Integer getSourceOfSupplyId() {
		return sourceOfSupplyId;
	}
	public void setSourceOfSupplyId(Integer sourceOfSupplyId) {
		this.sourceOfSupplyId = sourceOfSupplyId;
	}
	public Integer getTdsId() {
		return tdsId;
	}
	public void setTdsId(Integer tdsId) {
		this.tdsId = tdsId;
	}
	public String getOpeningBalance() {
		return openingBalance;
	}
	public void setOpeningBalance(String openingBalance) {
		this.openingBalance = openingBalance;
	}
}
