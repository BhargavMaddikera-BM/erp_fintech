package com.blackstrawai.settings;

import java.util.List;

import com.blackstrawai.ap.dropdowns.BasicCurrencyVo;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsVo;

public class ProductDropDownVo {

	private List<UnitOfMeasureVo>unitOfMeasure;	
	private List<TaxGroupVo> inter;
	private List<TaxGroupVo> intra;
	private List<MinimalChartOfAccountsVo> purchaseAccount;
	private List<MinimalChartOfAccountsVo>  salesAccount;	
	private List<BasicCurrencyVo> currency;
	private BasicCurrencyVo defaultCurrency;
	private List<ProductCategoryVo> category;
	
	
	public List<ProductCategoryVo> getCategory() {
		return category;
	}
	public void setCategory(List<ProductCategoryVo> category) {
		this.category = category;
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
	public List<MinimalChartOfAccountsVo> getPurchaseAccount() {
		return purchaseAccount;
	}
	public void setPurchaseAccount(List<MinimalChartOfAccountsVo> purchaseAccount) {
		this.purchaseAccount = purchaseAccount;
	}
	public List<MinimalChartOfAccountsVo> getSalesAccount() {
		return salesAccount;
	}
	public void setSalesAccount(List<MinimalChartOfAccountsVo> salesAccount) {
		this.salesAccount = salesAccount;
	}
	public List<UnitOfMeasureVo> getUnitOfMeasure() {
		return unitOfMeasure;
	}
	public void setUnitOfMeasure(List<UnitOfMeasureVo> unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}
	public List<TaxGroupVo> getInter() {
		return inter;
	}
	public void setInter(List<TaxGroupVo> inter) {
		this.inter = inter;
	}
	public List<TaxGroupVo> getIntra() {
		return intra;
	}
	public void setIntra(List<TaxGroupVo> intra) {
		this.intra = intra;
	}
}
