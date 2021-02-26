package com.blackstrawai.request.onboarding.organization;

import java.util.List;

public class KeyMembersRequest {

	private List<OrganizationProprietorRequest> organizationProprietor;
	private List<OrganizationPartnershipPartnerRequest> organizationPartnershipPartner;
	private List<OrganizationPrivateLimitedDirectorRequest> organizationPrivateLimitedDirector;
	private List<OrganizationOnePersonDirectorRequest> organizationOnePersonDirector;
	private List<OrganizationLimitedLiabilityPartnerRequest>organizationLimitedLiabilityPartner;
	private List<OrganizationPublicLimitedDirectorRequest>organizationPublicLimitedDirector;
	public List<OrganizationProprietorRequest> getOrganizationProprietor() {
		return organizationProprietor;
	}
	public void setOrganizationProprietor(List<OrganizationProprietorRequest> organizationProprietor) {
		this.organizationProprietor = organizationProprietor;
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
	public List<OrganizationOnePersonDirectorRequest> getOrganizationOnePersonDirector() {
		return organizationOnePersonDirector;
	}
	public void setOrganizationOnePersonDirector(List<OrganizationOnePersonDirectorRequest> organizationOnePersonDirector) {
		this.organizationOnePersonDirector = organizationOnePersonDirector;
	}
	public List<OrganizationLimitedLiabilityPartnerRequest> getOrganizationLimitedLiabilityPartner() {
		return organizationLimitedLiabilityPartner;
	}
	public void setOrganizationLimitedLiabilityPartner(
			List<OrganizationLimitedLiabilityPartnerRequest> organizationLimitedLiabilityPartner) {
		this.organizationLimitedLiabilityPartner = organizationLimitedLiabilityPartner;
	}
	public List<OrganizationPublicLimitedDirectorRequest> getOrganizationPublicLimitedDirector() {
		return organizationPublicLimitedDirector;
	}
	public void setOrganizationPublicLimitedDirector(
			List<OrganizationPublicLimitedDirectorRequest> organizationPublicLimitedDirector) {
		this.organizationPublicLimitedDirector = organizationPublicLimitedDirector;
	}
}
