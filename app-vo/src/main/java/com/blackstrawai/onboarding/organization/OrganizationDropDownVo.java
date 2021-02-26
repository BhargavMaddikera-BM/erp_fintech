package com.blackstrawai.onboarding.organization;

import java.util.List;

import com.blackstrawai.ap.dropdowns.BasicCurrencyVo;
import com.blackstrawai.common.BaseVo;
import com.blackstrawai.common.CountryVo;

public class OrganizationDropDownVo extends BaseVo{
	
	private List<OrganizationTypeVo>organizationType;
	private List<OrganizationConstitutionVo>organizationConstitution;
	private List<BasicCurrencyVo>currency;
	private List<CountryVo>country;
	public List<CountryVo> getCountry() {
		return country;
	}
	public void setCountry(List<CountryVo> country) {
		this.country = country;
	}
	public List<OrganizationTypeVo> getOrganizationType() {
		return organizationType;
	}
	public void setOrganizationType(List<OrganizationTypeVo> organizationType) {
		this.organizationType = organizationType;
	}	
	public List<OrganizationConstitutionVo> getOrganizationConstitution() {
		return organizationConstitution;
	}
	public void setOrganizationConstitution(List<OrganizationConstitutionVo> organizationConstitution) {
		this.organizationConstitution = organizationConstitution;
	}
	public List<BasicCurrencyVo> getCurrency() {
		return currency;
	}
	public void setCurrency(List<BasicCurrencyVo> currency) {
		this.currency = currency;
	}

}
