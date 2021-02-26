package com.blackstrawai.ap.dropdowns;

import java.util.List;



import com.blackstrawai.onboarding.organization.BasicGSTLocationDetailsVo;
import com.blackstrawai.settings.TdsVo;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsVo;

public class InvoiceDropDownVo {
	
	
	private List<BasicVendorVo> basicVendor;
	private String dateFormat;
	private List<BasicPaymentTermsVo> paymentTerms;
	private List<SourceOfSupplyVo> sourceOfSupply; 
	private List<SourceOfSupplyVo> destinationOfSupply;
	private List<ItemTaxRateVo> taxApplicationMethod;
	private List<BasicCurrencyVo> currency;
	private List<BasicProductVo> products;
	private List<BasicUnitOfMeasureVo> measures;
	private List<TaxRateVo> taxGroups;
	private List<BasicCustomerVo> customers;
	private List<DiscountTypeVo> discountTypes;
	private List<TdsVo> tds;
	private BasicCurrencyVo defaultCurrency;
	private List<MinimalChartOfAccountsVo> chartOfAccounts;
	private List<MinimalChartOfAccountsVo> discountAccounts;
	private List<MinimalChartOfAccountsVo> adjustmentAccounts;
	private BasicGSTLocationDetailsVo locationGst;
	private BasicVoucherEntriesVo voucherEntries;
	private String aksharUrl;
	
	
	public BasicVoucherEntriesVo getVoucherEntries() {
		return voucherEntries;
	}
	public void setVoucherEntries(BasicVoucherEntriesVo voucherEntries) {
		this.voucherEntries = voucherEntries;
	}
	public String getAksharUrl() {
		return aksharUrl;
	}
	public void setAksharUrl(String aksharUrl) {
		this.aksharUrl = aksharUrl;
	}
	public List<MinimalChartOfAccountsVo> getChartOfAccounts() {
		return chartOfAccounts;
	}
	public void setChartOfAccounts(List<MinimalChartOfAccountsVo> chartOfAccounts) {
		this.chartOfAccounts = chartOfAccounts;
	}
	public List<MinimalChartOfAccountsVo> getDiscountAccounts() {
		return discountAccounts;
	}
	public void setDiscountAccounts(List<MinimalChartOfAccountsVo> discountAccounts) {
		this.discountAccounts = discountAccounts;
	}
	public List<MinimalChartOfAccountsVo> getAdjustmentAccounts() {
		return adjustmentAccounts;
	}
	public void setAdjustmentAccounts(List<MinimalChartOfAccountsVo> adjustmentAccounts) {
		this.adjustmentAccounts = adjustmentAccounts;
	}
	
	public List<BasicVendorVo> getBasicVendor() {
		return basicVendor;
	}
	public void setBasicVendor(List<BasicVendorVo> basicVendor) {
		this.basicVendor = basicVendor;
	}
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	public List<BasicPaymentTermsVo> getPaymentTerms() {
		return paymentTerms;
	}
	public void setPaymentTerms(List<BasicPaymentTermsVo> paymentTerms) {
		this.paymentTerms = paymentTerms;
	}
	public List<SourceOfSupplyVo> getSourceOfSupply() {
		return sourceOfSupply;
	}
	public void setSourceOfSupply(List<SourceOfSupplyVo> sourceOfSupply) {
		this.sourceOfSupply = sourceOfSupply;
	}
	public List<SourceOfSupplyVo> getDestinationOfSupply() {
		return destinationOfSupply;
	}
	public void setDestinationOfSupply(List<SourceOfSupplyVo> destinationOfSupply) {
		this.destinationOfSupply = destinationOfSupply;
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
	public List<BasicCustomerVo> getCustomers() {
		return customers;
	}
	public void setCustomers(List<BasicCustomerVo> customers) {
		this.customers = customers;
	}
	public List<DiscountTypeVo> getDiscountTypes() {
		return discountTypes;
	}
	public void setDiscountTypes(List<DiscountTypeVo> discountTypes) {
		this.discountTypes = discountTypes;
	}

	public BasicCurrencyVo getDefaultCurrency() {
		return defaultCurrency;
	}
	public void setDefaultCurrency(BasicCurrencyVo defaultCurrency) {
		this.defaultCurrency = defaultCurrency;
	}
	
	public BasicGSTLocationDetailsVo getLocationGst() {
		return locationGst;
	}
	public void setLocationGst(BasicGSTLocationDetailsVo locationGst) {
		this.locationGst = locationGst;
	}
	public List<TdsVo> getTds() {
		return tds;
	}
	public void setTds(List<TdsVo> tds) {
		this.tds = tds;
	}
	
	
	
	
	
}


