package com.blackstrawai.request.onboarding.organization;

import java.util.List;

import com.blackstrawai.common.BaseRequest;

public class OrganizationRequest extends BaseRequest{
	
	private int id;
	private String name;
	private int organizationConstitution;
	private int organizationType;
	private int organizationIndustry;
	private int baseCurrency;
	public OrganizationAddressRequest getOrganizationAddress() {
		return organizationAddress;
	}
	public void setOrganizationAddress(OrganizationAddressRequest organizationAddress) {
		this.organizationAddress = organizationAddress;
	}
	private String financialYear;
	private String status;
	
	private OrganizationAddressRequest organizationAddress;
	
	public List<OrganizationProprietorRequest> getOrganizationProprietor() {
		return organizationProprietor;
	}
	public void setOrganizationProprietor(List<OrganizationProprietorRequest> organizationProprietor) {
		this.organizationProprietor = organizationProprietor;
	}
	private OrganizationProprietorOtherDetailsRequest organizationProprietorOtherDetails;
	private List<OrganizationProprietorRequest> organizationProprietor;
	
	private OrganizationPartnershipOtherDetailsRequest organizationPartnershipOtherDetails;
	private List<OrganizationPartnershipPartnerRequest> organizationPartnershipPartner;
	
	private OrganizationPrivateLimitedOtherDetailsRequest organizationPrivateLimitedOtherDetails;
	private List<OrganizationPrivateLimitedDirectorRequest> organizationPrivateLimitedDirector;
	
	private OrganizationOnePersonOtherDetailsRequest organizationOnePersonOtherDetails;
	private List<OrganizationOnePersonDirectorRequest> organizationOnePersonDirector;
	
	private OrganizationLimitedLiabilityOtherDetailsRequest organizationLimitedLiabilityOtherDetails;
	private List<OrganizationLimitedLiabilityPartnerRequest>organizationLimitedLiabilityPartner;
	
	private OrganizationPublicLimitedOtherDetailsRequest organizationPublicLimitedOtherDetails;
	private List<OrganizationPublicLimitedDirectorRequest>organizationPublicLimitedDirector;
	
	public OrganizationPublicLimitedOtherDetailsRequest getOrganizationPublicLimitedOtherDetails() {
		return organizationPublicLimitedOtherDetails;
	}
	public void setOrganizationPublicLimitedOtherDetails(
			OrganizationPublicLimitedOtherDetailsRequest organizationPublicLimitedOtherDetails) {
		this.organizationPublicLimitedOtherDetails = organizationPublicLimitedOtherDetails;
	}
	public List<OrganizationPublicLimitedDirectorRequest> getOrganizationPublicLimitedDirector() {
		return organizationPublicLimitedDirector;
	}
	public void setOrganizationPublicLimitedDirector(
			List<OrganizationPublicLimitedDirectorRequest> organizationPublicLimitedDirector) {
		this.organizationPublicLimitedDirector = organizationPublicLimitedDirector;
	}
	public OrganizationLimitedLiabilityOtherDetailsRequest getOrganizationLimitedLiabilityOtherDetails() {
		return organizationLimitedLiabilityOtherDetails;
	}
	public void setOrganizationLimitedLiabilityOtherDetails(
			OrganizationLimitedLiabilityOtherDetailsRequest organizationLimitedLiabilityOtherDetails) {
		this.organizationLimitedLiabilityOtherDetails = organizationLimitedLiabilityOtherDetails;
	}
	public List<OrganizationLimitedLiabilityPartnerRequest> getOrganizationLimitedLiabilityPartner() {
		return organizationLimitedLiabilityPartner;
	}
	public void setOrganizationLimitedLiabilityPartner(
			List<OrganizationLimitedLiabilityPartnerRequest> organizationLimitedLiabilityPartner) {
		this.organizationLimitedLiabilityPartner = organizationLimitedLiabilityPartner;
	}
	public OrganizationOnePersonOtherDetailsRequest getOrganizationOnePersonOtherDetails() {
		return organizationOnePersonOtherDetails;
	}
	public void setOrganizationOnePersonOtherDetails(
			OrganizationOnePersonOtherDetailsRequest organizationOnePersonOtherDetails) {
		this.organizationOnePersonOtherDetails = organizationOnePersonOtherDetails;
	}
	public List<OrganizationOnePersonDirectorRequest> getOrganizationOnePersonDirector() {
		return organizationOnePersonDirector;
	}
	public void setOrganizationOnePersonDirector(List<OrganizationOnePersonDirectorRequest> organizationOnePersonDirector) {
		this.organizationOnePersonDirector = organizationOnePersonDirector;
	}
	public List<OrganizationPartnershipPartnerRequest> getOrganizationPartnershipPartner() {
		return organizationPartnershipPartner;
	}
	public void setOrganizationPartnershipPartner(
			List<OrganizationPartnershipPartnerRequest> organizationPartnershipPartner) {
		this.organizationPartnershipPartner = organizationPartnershipPartner;
	}
	
	
	
	
	public List<OrganizationPrivateLimitedDirectorRequest> getOrganizationPrivateLimitedDirector() {
		return organizationPrivateLimitedDirector;
	}
	public void setOrganizationPrivateLimitedDirector(
			List<OrganizationPrivateLimitedDirectorRequest> organizationPrivateLimitedDirector) {
		this.organizationPrivateLimitedDirector = organizationPrivateLimitedDirector;
	}
	public OrganizationPrivateLimitedOtherDetailsRequest getOrganizationPrivateLimitedOtherDetails() {
		return organizationPrivateLimitedOtherDetails;
	}
	public void setOrganizationPrivateLimitedOtherDetails(
			OrganizationPrivateLimitedOtherDetailsRequest organizationPrivateLimitedOtherDetails) {
		this.organizationPrivateLimitedOtherDetails = organizationPrivateLimitedOtherDetails;
	}
	
	public OrganizationPartnershipOtherDetailsRequest getOrganizationPartnershipOtherDetails() {
		return organizationPartnershipOtherDetails;
	}
	public void setOrganizationPartnershipOtherDetails(
			OrganizationPartnershipOtherDetailsRequest organizationPartnershipOtherDetails) {
		this.organizationPartnershipOtherDetails = organizationPartnershipOtherDetails;
	}
	
	public OrganizationProprietorOtherDetailsRequest getOrganizationProprietorOtherDetails() {
		return organizationProprietorOtherDetails;
	}
	public void setOrganizationProprietorOtherDetails(
			OrganizationProprietorOtherDetailsRequest organizationProprietorOtherDetails) {
		this.organizationProprietorOtherDetails = organizationProprietorOtherDetails;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOrganizationConstitution() {
		return organizationConstitution;
	}
	public void setOrganizationConstitution(int organizationConstitution) {
		this.organizationConstitution = organizationConstitution;
	}
	public int getOrganizationType() {
		return organizationType;
	}
	public void setOrganizationType(int organizationType) {
		this.organizationType = organizationType;
	}
	public int getOrganizationIndustry() {
		return organizationIndustry;
	}
	public void setOrganizationIndustry(int organizationIndustry) {
		this.organizationIndustry = organizationIndustry;
	}
	public int getBaseCurrency() {
		return baseCurrency;
	}
	public void setBaseCurrency(int baseCurrency) {
		this.baseCurrency = baseCurrency;
	}
	public String getFinancialYear() {
		return financialYear;
	}
	public void setFinancialYear(String financialYear) {
		this.financialYear = financialYear;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	
}
