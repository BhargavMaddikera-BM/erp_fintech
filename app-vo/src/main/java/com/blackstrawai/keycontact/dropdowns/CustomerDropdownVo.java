package com.blackstrawai.keycontact.dropdowns;

import java.util.List;

import com.blackstrawai.ap.dropdowns.BasicCurrencyVo;
import com.blackstrawai.ap.dropdowns.BasicCustomerGroupVo;
import com.blackstrawai.ap.dropdowns.BasicPaymentTermsVo;
import com.blackstrawai.ap.dropdowns.GSTTreatmentVo;
import com.blackstrawai.ap.dropdowns.OrganizationConstitutionDropDownVo;
import com.blackstrawai.ap.dropdowns.TDSVo;
import com.blackstrawai.common.CountryVo;
import com.blackstrawai.onboarding.organization.BasicGSTLocationDetailsVo;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsVo;

public class CustomerDropdownVo {
	private List<OrganizationConstitutionDropDownVo> organizationConstitution;
	private List<BasicCustomerGroupVo> customerGroup;
	private List<GSTTreatmentVo> gstTreatment;
	private List<BasicCurrencyVo> currency;
	private List<BasicPaymentTermsVo> paymentTerms;
	private List<TDSVo> tds;
	private List<CountryVo> country;
	private BasicGSTLocationDetailsVo locationGst; 
	private List<MinimalChartOfAccountsVo> ledger;
	private Integer defaultGlLedgerId;
	
	public List<CountryVo> getCountry() {
		return country;
	}
	public void setCountry(List<CountryVo> country) {
		this.country = country;
	}
	public List<OrganizationConstitutionDropDownVo> getOrganizationConstitution() {
		return organizationConstitution;
	}
	public void setOrganizationConstitution(List<OrganizationConstitutionDropDownVo> organizationConstitution) {
		this.organizationConstitution = organizationConstitution;
	}
	
	public List<GSTTreatmentVo> getGstTreatment() {
		return gstTreatment;
	}
	public void setGstTreatment(List<GSTTreatmentVo> gstTreatment) {
		this.gstTreatment = gstTreatment;
	}
	public List<BasicCurrencyVo> getCurrency() {
		return currency;
	}
	public void setCurrency(List<BasicCurrencyVo> currency) {
		this.currency = currency;
	}
	public List<BasicCustomerGroupVo> getCustomerGroup() {
		return customerGroup;
	}
	public void setCustomerGroup(List<BasicCustomerGroupVo> customerGroup) {
		this.customerGroup = customerGroup;
	}
	public List<BasicPaymentTermsVo> getPaymentTerms() {
		return paymentTerms;
	}
	public void setPaymentTerms(List<BasicPaymentTermsVo> paymentTerms) {
		this.paymentTerms = paymentTerms;
	}
	public List<TDSVo> getTds() {
		return tds;
	}
	public void setTds(List<TDSVo> tds) {
		this.tds = tds;
	}
	public BasicGSTLocationDetailsVo getLocationGst() {
		return locationGst;
	}
	public void setLocationGst(BasicGSTLocationDetailsVo locationGst) {
		this.locationGst = locationGst;
	}
	public List<MinimalChartOfAccountsVo> getLedger() {
		return ledger;
	}
	public void setLedger(List<MinimalChartOfAccountsVo> ledger) {
		this.ledger = ledger;
	}
	public Integer getDefaultGlLedgerId() {
		return defaultGlLedgerId;
	}
	public void setDefaultGlLedgerId(Integer defaultGlLedgerId) {
		this.defaultGlLedgerId = defaultGlLedgerId;
	}
	
	
}
