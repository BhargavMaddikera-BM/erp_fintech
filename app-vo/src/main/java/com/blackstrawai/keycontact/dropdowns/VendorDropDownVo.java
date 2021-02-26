package com.blackstrawai.keycontact.dropdowns;

import java.util.List;

import com.blackstrawai.ap.dropdowns.BasicCurrencyVo;
import com.blackstrawai.ap.dropdowns.BasicPaymentTermsVo;
import com.blackstrawai.ap.dropdowns.BasicVendorGroupVo;
import com.blackstrawai.ap.dropdowns.GSTTreatmentVo;
import com.blackstrawai.ap.dropdowns.OrganizationConstitutionDropDownVo;
import com.blackstrawai.ap.dropdowns.SourceOfSupplyVo;
import com.blackstrawai.common.CountryVo;
import com.blackstrawai.common.DocumentTypeVo;
import com.blackstrawai.onboarding.organization.BasicGSTLocationDetailsVo;
import com.blackstrawai.settings.TdsVo;
import com.blackstrawai.settings.chartofaccounts.MinimalChartOfAccountsVo;

public class VendorDropDownVo {
	private List<GSTTreatmentVo> gstTreatment;
	private List<SourceOfSupplyVo> sourceOfSupply;
	private List<BasicCurrencyVo> currency;
	private List<OrganizationConstitutionDropDownVo> organizationConstitution;
	private List<BasicVendorGroupVo> vendorGroup;
	private List<TdsVo> tds;
	private List<CountryVo> country;
	private List<DocumentTypeVo> documentTypes;
	private BasicGSTLocationDetailsVo locationGst;  
	private List<String> addressType;
	private List<MinimalChartOfAccountsVo> ledger;
	private List<BasicPaymentTermsVo> paymentTerms;
	private Integer defaultGlLedgerId;
	
	public List<String> getAddressType() {
		return addressType;
	}

	public void setAddressType(List<String> addressType) {
		this.addressType = addressType;
	}

	public BasicGSTLocationDetailsVo getLocationGst() {
		return locationGst;
	}

	public void setLocationGst(BasicGSTLocationDetailsVo locationGst) {
		this.locationGst = locationGst;
	}

	public List<DocumentTypeVo> getDocumentTypes() {
		return documentTypes;
	}

	public void setDocumentTypes(List<DocumentTypeVo> documentTypes) {
		this.documentTypes = documentTypes;
	}

	public List<CountryVo> getCountry() {
		return country;
	}

	public void setCountry(List<CountryVo> country) {
		this.country = country;
	}

	public List<TdsVo> getTds() {
		return tds;
	}

	public void setTds(List<TdsVo> tds) {
		this.tds = tds;
	}

	public List<OrganizationConstitutionDropDownVo> getOrganizationConstitution() {
		return organizationConstitution;
	}

	public void setOrganizationConstitution(List<OrganizationConstitutionDropDownVo> organizationConstitution) {
		this.organizationConstitution = organizationConstitution;
	}

	public List<BasicVendorGroupVo> getVendorGroup() {
		return vendorGroup;
	}

	public void setVendorGroup(List<BasicVendorGroupVo> vendorGroup) {
		this.vendorGroup = vendorGroup;
	}

	public List<GSTTreatmentVo> getGstTreatment() {
		return gstTreatment;
	}

	public void setGstTreatment(List<GSTTreatmentVo> gstTreatment) {
		this.gstTreatment = gstTreatment;
	}

	public List<SourceOfSupplyVo> getSourceOfSupply() {
		return sourceOfSupply;
	}

	public void setSourceOfSupply(List<SourceOfSupplyVo> sourceOfSupply) {
		this.sourceOfSupply = sourceOfSupply;
	}

	public List<BasicCurrencyVo> getCurrency() {
		return currency;
	}

	public void setCurrency(List<BasicCurrencyVo> currency) {
		this.currency = currency;
	}

	public List<BasicPaymentTermsVo> getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(List<BasicPaymentTermsVo> paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public Integer getDefaultGlLedgerId() {
		return defaultGlLedgerId;
	}

	public void setDefaultGlLedgerId(Integer defaultGlLedgerId) {
		this.defaultGlLedgerId = defaultGlLedgerId;
	}

	public List<MinimalChartOfAccountsVo> getLedger() {
		return ledger;
	}

	public void setLedger(List<MinimalChartOfAccountsVo> ledger) {
		this.ledger = ledger;
	}

}
