package com.blackstrawai.externalintegration.general.instafinancials;

import java.util.List;

public class InstaFinancialsVo {

	private String organizationName;
	private String organizationType;
	private String industryOfOrganization;
	private int organizationTypeId;
	private int industryOfOrganizationId;
	private String industryGroupOfOrganization;
	private String email;
	private String taxId;
	private String registeredAddress;
	private String dateOfIncorporation;
	private List<InstaFinancialsDirectorVo> directorDetails;

	public int getOrganizationTypeId() {
		return organizationTypeId;
	}

	public void setOrganizationTypeId(int organizationTypeId) {
		this.organizationTypeId = organizationTypeId;
	}

	public int getIndustryOfOrganizationId() {
		return industryOfOrganizationId;
	}

	public void setIndustryOfOrganizationId(int industryOfOrganizationId) {
		this.industryOfOrganizationId = industryOfOrganizationId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public String getIndustryGroupOfOrganization() {
		return industryGroupOfOrganization;
	}

	public void setIndustryGroupOfOrganization(String industryGroupOfOrganization) {
		this.industryGroupOfOrganization = industryGroupOfOrganization;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(String organizationType) {
		this.organizationType = organizationType;
	}

	public String getIndustryOfOrganization() {
		return industryOfOrganization;
	}

	public void setIndustryOfOrganization(String industryOfOrganization) {
		this.industryOfOrganization = industryOfOrganization;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public String getRegisteredAddress() {
		return registeredAddress;
	}

	public void setRegisteredAddress(String registeredAddress) {
		this.registeredAddress = registeredAddress;
	}

	public String getDateOfIncorporation() {
		return dateOfIncorporation;
	}

	public void setDateOfIncorporation(String dateOfIncorporation) {
		this.dateOfIncorporation = dateOfIncorporation;
	}

	public List<InstaFinancialsDirectorVo> getDirectorDetails() {
		return directorDetails;
	}

	public void setDirectorDetails(List<InstaFinancialsDirectorVo> directorDetails) {
		this.directorDetails = directorDetails;
	}

	
}
