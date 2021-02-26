package com.blackstrawai.ap.dropdowns;

import java.util.List;

import com.blackstrawai.common.CountryVo;
import com.blackstrawai.common.TokenVo;
import com.blackstrawai.onboarding.organization.BasicGSTLocationDetailsVo;
import com.blackstrawai.settings.TdsVo;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsVo;

public class PurchaseOrderDropDownVo extends TokenVo{
	private List<BasicVendorDetailsVo> basicVendor;
	private BasicVoucherEntriesVo voucherEntries;
	private String dateFormat;
	private BasicGSTLocationDetailsVo locationGst;
	private List<PurchaseOrderTypeVo> purchaseOrderType;
	private List<BasicShippingPreferenceVo> shippingPreference;
	private List<ShippingMethodVo> shippingMethod;
	private List<BasicPaymentTermsVo> paymentTerms;
	private List<CountryVo>country;
	private List<SourceOfSupplyVo> sourceOfSupply; 
	private List<ItemTaxRateVo> taxApplicationMethod;
	private List<BasicCurrencyVo> currency;
	private List<BasicProductVo> products;
	private List<MinimalChartOfAccountsVo> chartOfAccounts;
	private List<BasicUnitOfMeasureVo> measures;
	private List<TaxRateVo> taxGroups;
	private List<DiscountTypeVo> discountTypes;
	private List<TdsVo> tds;
	private BasicCurrencyVo defaultCurrency;
	
	public List<BasicVendorDetailsVo> getBasicVendor() {
		return basicVendor;
	}
	public void setBasicVendor(List<BasicVendorDetailsVo> basicVendor) {
		this.basicVendor = basicVendor;
	}
	public BasicVoucherEntriesVo getVoucherEntries() {
		return voucherEntries;
	}
	public void setVoucherEntries(BasicVoucherEntriesVo voucherEntries) {
		this.voucherEntries = voucherEntries;
	}
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	public BasicGSTLocationDetailsVo getLocationGst() {
		return locationGst;
	}
	public void setLocationGst(BasicGSTLocationDetailsVo locationGst) {
		this.locationGst = locationGst;
	}
	public List<PurchaseOrderTypeVo> getPurchaseOrderType() {
		return purchaseOrderType;
	}
	public void setPurchaseOrderType(List<PurchaseOrderTypeVo> purchaseOrderType) {
		this.purchaseOrderType = purchaseOrderType;
	}
	public List<BasicShippingPreferenceVo> getShippingPreference() {
		return shippingPreference;
	}
	public void setShippingPreference(List<BasicShippingPreferenceVo> shippingPreference) {
		this.shippingPreference = shippingPreference;
	}
	public List<ShippingMethodVo> getShippingMethod() {
		return shippingMethod;
	}
	public void setShippingMethod(List<ShippingMethodVo> shippingMethod) {
		this.shippingMethod = shippingMethod;
	}
	public List<BasicPaymentTermsVo> getPaymentTerms() {
		return paymentTerms;
	}
	public void setPaymentTerms(List<BasicPaymentTermsVo> paymentTerms) {
		this.paymentTerms = paymentTerms;
	}
	public List<CountryVo> getCountry() {
		return country;
	}
	public void setCountry(List<CountryVo> country) {
		this.country = country;
	}
	public List<SourceOfSupplyVo> getSourceOfSupply() {
		return sourceOfSupply;
	}
	public void setSourceOfSupply(List<SourceOfSupplyVo> sourceOfSupply) {
		this.sourceOfSupply = sourceOfSupply;
	}
	public List<ItemTaxRateVo> getTaxApplicationMethod() {
		return taxApplicationMethod;
	}
	public void setTaxApplicationMethod(List<ItemTaxRateVo> taxApplicationMethod) {
		this.taxApplicationMethod = taxApplicationMethod;
	}
	public List<BasicCurrencyVo> getCurrency() {
		return currency;
	}
	public void setCurrency(List<BasicCurrencyVo> currency) {
		this.currency = currency;
	}
	public List<BasicProductVo> getProducts() {
		return products;
	}
	public void setProducts(List<BasicProductVo> products) {
		this.products = products;
	}
	public List<MinimalChartOfAccountsVo> getChartOfAccounts() {
		return chartOfAccounts;
	}
	public void setChartOfAccounts(List<MinimalChartOfAccountsVo> chartOfAccounts) {
		this.chartOfAccounts = chartOfAccounts;
	}
	public List<BasicUnitOfMeasureVo> getMeasures() {
		return measures;
	}
	public void setMeasures(List<BasicUnitOfMeasureVo> measures) {
		this.measures = measures;
	}
	public List<TaxRateVo> getTaxGroups() {
		return taxGroups;
	}
	public void setTaxGroups(List<TaxRateVo> taxGroups) {
		this.taxGroups = taxGroups;
	}
	public List<DiscountTypeVo> getDiscountTypes() {
		return discountTypes;
	}
	public void setDiscountTypes(List<DiscountTypeVo> discountTypes) {
		this.discountTypes = discountTypes;
	}
	public List<TdsVo> getTds() {
		return tds;
	}
	public void setTds(List<TdsVo> tds) {
		this.tds = tds;
	}
	public BasicCurrencyVo getDefaultCurrency() {
		return defaultCurrency;
	}
	public void setDefaultCurrency(BasicCurrencyVo defaultCurrency) {
		this.defaultCurrency = defaultCurrency;
	}
	
	
	
	
	
	
}
