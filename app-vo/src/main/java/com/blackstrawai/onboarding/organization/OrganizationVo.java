package com.blackstrawai.onboarding.organization;

import java.sql.Timestamp;
import java.util.List;

import com.blackstrawai.common.TokenVo;

public class OrganizationVo extends TokenVo{
	
	private int id;
	private String name;
	private int organizationConstitution;
	private int organizationType;
	private int organizationIndustry;
	private int baseCurrency;
	private Timestamp createTs;
	private Timestamp updateTs;
	private int applications;
	private int users;
	
	public int getApplications() {
		return applications;
	}
	public void setApplications(int applications) {
		this.applications = applications;
	}
	public int getUsers() {
		return users;
	}
	public void setUsers(int users) {
		this.users = users;
	}
	public Timestamp getCreateTs() {
		return createTs;
	}
	public void setCreateTs(Timestamp createTs) {
		this.createTs = createTs;
	}
	public Timestamp getUpdateTs() {
		return updateTs;
	}
	public void setUpdateTs(Timestamp updateTs) {
		this.updateTs = updateTs;
	}
	private String financialYear;
	private String status;
	
	private OrganizationAddressVo organizationAddress;
	
	
	private OrganizationProprietorOtherDetailsVo organizationProprietorOtherDetails;
	private List<OrganizationProprietorVo> organizationProprietor;
	
	private OrganizationPartnershipOtherDetailsVo organizationPartnershipOtherDetails;
	private List<OrganizationPartnershipPartnerVo> organizationPartnershipPartner;
	
	private OrganizationPrivateLimitedOtherDetailsVo organizationPrivateLimitedOtherDetails;
	private List<OrganizationPrivateLimitedDirectorVo> organizationPrivateLimitedDirector;
	
	private OrganizationOnePersonOtherDetailsVo organizationOnePersonOtherDetails;
	public OrganizationAddressVo getOrganizationAddress() {
		return organizationAddress;
	}
	public void setOrganizationAddress(OrganizationAddressVo organizationAddress) {
		this.organizationAddress = organizationAddress;
	}
	public OrganizationProprietorOtherDetailsVo getOrganizationProprietorOtherDetails() {
		return organizationProprietorOtherDetails;
	}
	public void setOrganizationProprietorOtherDetails(
			OrganizationProprietorOtherDetailsVo organizationProprietorOtherDetails) {
		this.organizationProprietorOtherDetails = organizationProprietorOtherDetails;
	}
	public List<OrganizationProprietorVo> getOrganizationProprietor() {
		return organizationProprietor;
	}
	public void setOrganizationProprietor(List<OrganizationProprietorVo> organizationProprietor) {
		this.organizationProprietor = organizationProprietor;
	}
	public OrganizationPartnershipOtherDetailsVo getOrganizationPartnershipOtherDetails() {
		return organizationPartnershipOtherDetails;
	}
	public void setOrganizationPartnershipOtherDetails(
			OrganizationPartnershipOtherDetailsVo organizationPartnershipOtherDetails) {
		this.organizationPartnershipOtherDetails = organizationPartnershipOtherDetails;
	}
	public List<OrganizationPartnershipPartnerVo> getOrganizationPartnershipPartner() {
		return organizationPartnershipPartner;
	}
	public void setOrganizationPartnershipPartner(List<OrganizationPartnershipPartnerVo> organizationPartnershipPartner) {
		this.organizationPartnershipPartner = organizationPartnershipPartner;
	}
	public OrganizationPrivateLimitedOtherDetailsVo getOrganizationPrivateLimitedOtherDetails() {
		return organizationPrivateLimitedOtherDetails;
	}
	public void setOrganizationPrivateLimitedOtherDetails(
			OrganizationPrivateLimitedOtherDetailsVo organizationPrivateLimitedOtherDetails) {
		this.organizationPrivateLimitedOtherDetails = organizationPrivateLimitedOtherDetails;
	}
	public List<OrganizationPrivateLimitedDirectorVo> getOrganizationPrivateLimitedDirector() {
		return organizationPrivateLimitedDirector;
	}
	public void setOrganizationPrivateLimitedDirector(
			List<OrganizationPrivateLimitedDirectorVo> organizationPrivateLimitedDirector) {
		this.organizationPrivateLimitedDirector = organizationPrivateLimitedDirector;
	}
	public OrganizationOnePersonOtherDetailsVo getOrganizationOnePersonOtherDetails() {
		return organizationOnePersonOtherDetails;
	}
	public void setOrganizationOnePersonOtherDetails(
			OrganizationOnePersonOtherDetailsVo organizationOnePersonOtherDetails) {
		this.organizationOnePersonOtherDetails = organizationOnePersonOtherDetails;
	}
	public List<OrganizationOnePersonDirectorVo> getOrganizationOnePersonDirector() {
		return organizationOnePersonDirector;
	}
	public void setOrganizationOnePersonDirector(List<OrganizationOnePersonDirectorVo> organizationOnePersonDirector) {
		this.organizationOnePersonDirector = organizationOnePersonDirector;
	}
	public OrganizationLimitedLiabilityOtherDetailsVo getOrganizationLimitedLiabilityOtherDetails() {
		return organizationLimitedLiabilityOtherDetails;
	}
	public void setOrganizationLimitedLiabilityOtherDetails(
			OrganizationLimitedLiabilityOtherDetailsVo organizationLimitedLiabilityOtherDetails) {
		this.organizationLimitedLiabilityOtherDetails = organizationLimitedLiabilityOtherDetails;
	}
	public List<OrganizationLimitedLiabilityPartnerVo> getOrganizationLimitedLiabilityPartner() {
		return organizationLimitedLiabilityPartner;
	}
	public void setOrganizationLimitedLiabilityPartner(
			List<OrganizationLimitedLiabilityPartnerVo> organizationLimitedLiabilityPartner) {
		this.organizationLimitedLiabilityPartner = organizationLimitedLiabilityPartner;
	}
	public OrganizationPublicLimitedOtherDetailsVo getOrganizationPublicLimitedOtherDetails() {
		return organizationPublicLimitedOtherDetails;
	}
	public void setOrganizationPublicLimitedOtherDetails(
			OrganizationPublicLimitedOtherDetailsVo organizationPublicLimitedOtherDetails) {
		this.organizationPublicLimitedOtherDetails = organizationPublicLimitedOtherDetails;
	}
	public List<OrganizationPublicLimitedDirectorVo> getOrganizationPublicLimitedDirector() {
		return organizationPublicLimitedDirector;
	}
	public void setOrganizationPublicLimitedDirector(
			List<OrganizationPublicLimitedDirectorVo> organizationPublicLimitedDirector) {
		this.organizationPublicLimitedDirector = organizationPublicLimitedDirector;
	}
	private List<OrganizationOnePersonDirectorVo> organizationOnePersonDirector;
	
	private OrganizationLimitedLiabilityOtherDetailsVo organizationLimitedLiabilityOtherDetails;
	private List<OrganizationLimitedLiabilityPartnerVo>organizationLimitedLiabilityPartner;
	
	private OrganizationPublicLimitedOtherDetailsVo organizationPublicLimitedOtherDetails;
	private List<OrganizationPublicLimitedDirectorVo>organizationPublicLimitedDirector;
	
	
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
