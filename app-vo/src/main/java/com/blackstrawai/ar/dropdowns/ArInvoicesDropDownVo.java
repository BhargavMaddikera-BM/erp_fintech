package com.blackstrawai.ar.dropdowns;

import java.util.List;

import com.blackstrawai.ap.dropdowns.BasicCurrencyVo;
import com.blackstrawai.ap.dropdowns.BasicCustomerVo;
import com.blackstrawai.ap.dropdowns.BasicPaymentTermsVo;
import com.blackstrawai.ap.dropdowns.BasicProductVo;
import com.blackstrawai.ap.dropdowns.BasicUnitOfMeasureVo;
import com.blackstrawai.ap.dropdowns.BasicVoucherEntriesVo;
import com.blackstrawai.ap.dropdowns.DiscountTypeVo;
import com.blackstrawai.ap.dropdowns.ItemTaxRateVo;
import com.blackstrawai.ap.dropdowns.SourceOfSupplyVo;
import com.blackstrawai.ap.dropdowns.TDSVo;
import com.blackstrawai.ap.dropdowns.TaxRateVo;
import com.blackstrawai.banking.dashboard.BasicBankingVo;
import com.blackstrawai.common.CountryVo;
import com.blackstrawai.onboarding.organization.BasicGSTLocationDetailsVo;
import com.blackstrawai.settings.CommonVo;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsVo;

public class ArInvoicesDropDownVo {

	private List<CommonVo> invoiceType;
	
	private List<BasicCustomerVo> customers;
	
	private BasicGSTLocationDetailsVo locationDetails;
	
	private List<SourceOfSupplyVo> sourceOfSupply;
	
	private List<CommonVo> supplyServices;

	private List<BasicPaymentTermsVo> paymentTerms;
	
	private List<BasicProductVo> products;
	
	List<BasicCurrencyVo> currency;
	
	private List<BasicUnitOfMeasureVo> measures;
	
	private List<TaxRateVo> taxGroups;
	
	private List<DiscountTypeVo> discountTypes;
	
	private List<TDSVo> tds;
	
	private List<MinimalChartOfAccountsVo> chartOfAccounts;
	
	private List<MinimalChartOfAccountsVo> discountAccounts;
	
	private List<MinimalChartOfAccountsVo> adjustmentAccounts;
	
	private List<BasicLutVo> lutDetails;
	
	private List<CommonVo> exportType;
	
	private BasicBankingVo bankingDetails;

	private List<CountryVo> country;
	
	private BasicVoucherEntriesVo voucherEntries;
	
	private  List<ItemTaxRateVo>  taxApplicationMethod;
	
	private BasicCurrencyVo defaultCurrency;
	
	public List<CommonVo> getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(List<CommonVo> invoiceType) {
		this.invoiceType = invoiceType;
	}

	public List<BasicCustomerVo> getCustomers() {
		return customers;
	}

	public void setCustomers(List<BasicCustomerVo> customers) {
		this.customers = customers;
	}

	public List<SourceOfSupplyVo> getSourceOfSupply() {
		return sourceOfSupply;
	}

	public void setSourceOfSupply(List<SourceOfSupplyVo> sourceOfSupply) {
		this.sourceOfSupply = sourceOfSupply;
	}

	public List<CommonVo> getSupplyServices() {
		return supplyServices;
	}

	public void setSupplyServices(List<CommonVo> supplyServices) {
		this.supplyServices = supplyServices;
	}

	public List<BasicPaymentTermsVo> getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(List<BasicPaymentTermsVo> paymentTerms) {
		this.paymentTerms = paymentTerms;
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

	public List<DiscountTypeVo> getDiscountTypes() {
		return discountTypes;
	}

	public void setDiscountTypes(List<DiscountTypeVo> discountTypes) {
		this.discountTypes = discountTypes;
	}

	public List<TDSVo> getTds() {
		return tds;
	}

	public void setTds(List<TDSVo> tds) {
		this.tds = tds;
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

	public List<BasicLutVo> getLutDetails() {
		return lutDetails;
	}

	public void setLutDetails(List<BasicLutVo> lutDetails) {
		this.lutDetails = lutDetails;
	}

	public List<CommonVo> getExportType() {
		return exportType;
	}

	public void setExportType(List<CommonVo> exportType) {
		this.exportType = exportType;
	}

	public BasicGSTLocationDetailsVo getLocationDetails() {
		return locationDetails;
	}

	public void setLocationDetails(BasicGSTLocationDetailsVo locationDetails) {
		this.locationDetails = locationDetails;
	}

	public BasicBankingVo getBankingDetails() {
		return bankingDetails;
	}

	public void setBankingDetails(BasicBankingVo bankingDetails) {
		this.bankingDetails = bankingDetails;
	}

	public List<CountryVo> getCountry() {
		return country;
	}

	public void setCountry(List<CountryVo> country) {
		this.country = country;
	}

	public BasicVoucherEntriesVo getVoucherEntries() {
		return voucherEntries;
	}

	public void setVoucherEntries(BasicVoucherEntriesVo voucherEntries) {
		this.voucherEntries = voucherEntries;
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

	public BasicCurrencyVo getDefaultCurrency() {
		return defaultCurrency;
	}

	public void setDefaultCurrency(BasicCurrencyVo defaultCurrency) {
		this.defaultCurrency = defaultCurrency;
	}


	
	
}
