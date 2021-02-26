package com.blackstrawai.helper;

import java.util.ArrayList;
import java.util.List;

import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.onboarding.loginandregistration.ChangePasswordVo;
import com.blackstrawai.onboarding.loginandregistration.LoginVo;
import com.blackstrawai.onboarding.loginandregistration.ProfileVo;
import com.blackstrawai.onboarding.loginandregistration.RecoverPasswordVo;
import com.blackstrawai.onboarding.loginandregistration.RegistrationVo;
import com.blackstrawai.onboarding.loginandregistration.ResetPasswordVo;
import com.blackstrawai.onboarding.organization.GSTDetailsVo;
import com.blackstrawai.onboarding.organization.KeyMembersVo;
import com.blackstrawai.onboarding.organization.LocationVo;
import com.blackstrawai.onboarding.organization.MinimalOrganizationVo;
import com.blackstrawai.onboarding.organization.NewOrganizationVo;
import com.blackstrawai.onboarding.organization.OrganizationGeneralInfoVo;
import com.blackstrawai.onboarding.organization.OrganizationLimitedLiabilityPartnerVo;
import com.blackstrawai.onboarding.organization.OrganizationOnePersonDirectorVo;
import com.blackstrawai.onboarding.organization.OrganizationPartnershipPartnerVo;
import com.blackstrawai.onboarding.organization.OrganizationPrivateLimitedDirectorVo;
import com.blackstrawai.onboarding.organization.OrganizationProprietorVo;
import com.blackstrawai.onboarding.organization.OrganizationPublicLimitedDirectorVo;
import com.blackstrawai.onboarding.role.RoleVo;
import com.blackstrawai.onboarding.user.InviteActionVo;
import com.blackstrawai.onboarding.user.InviteUserVo;
import com.blackstrawai.onboarding.user.RegisteredAddressVo;
import com.blackstrawai.onboarding.user.RoleUserVo;
import com.blackstrawai.onboarding.user.UserVo;
import com.blackstrawai.request.onboarding.loginandregistration.ChangePasswordRequest;
import com.blackstrawai.request.onboarding.loginandregistration.LoginRequest;
import com.blackstrawai.request.onboarding.loginandregistration.ProfileRequest;
import com.blackstrawai.request.onboarding.loginandregistration.RecoverPasswordRequest;
import com.blackstrawai.request.onboarding.loginandregistration.RegisterationRequest;
import com.blackstrawai.request.onboarding.loginandregistration.ResetPasswordRequest;
import com.blackstrawai.request.onboarding.organization.LocationRequest;
import com.blackstrawai.request.onboarding.organization.MinimalOrganizationRequest;
import com.blackstrawai.request.onboarding.organization.NewOrganizationRequest;
import com.blackstrawai.request.onboarding.organization.OrganizationLimitedLiabilityPartnerRequest;
import com.blackstrawai.request.onboarding.organization.OrganizationOnePersonDirectorRequest;
import com.blackstrawai.request.onboarding.organization.OrganizationPartnershipPartnerRequest;
import com.blackstrawai.request.onboarding.organization.OrganizationPrivateLimitedDirectorRequest;
import com.blackstrawai.request.onboarding.organization.OrganizationProprietorRequest;
import com.blackstrawai.request.onboarding.organization.OrganizationPublicLimitedDirectorRequest;
import com.blackstrawai.request.onboarding.role.RoleRequest;
import com.blackstrawai.request.onboarding.user.InviteActionRequest;
import com.blackstrawai.request.onboarding.user.InviteUserRequest;
import com.blackstrawai.request.onboarding.user.RoleUserRequest;
import com.blackstrawai.request.onboarding.user.UserRequest;

public class OnBoardingConvertToVoHelper {
	
	private static OnBoardingConvertToVoHelper onBoardingConvertToVoHelper;

	public static OnBoardingConvertToVoHelper getInstance() {
		if (onBoardingConvertToVoHelper == null) {
			onBoardingConvertToVoHelper = new OnBoardingConvertToVoHelper();
		}
		return onBoardingConvertToVoHelper;
	}

	public LoginVo convertLoginVoFromLoginRequest(LoginRequest loginRequest) {
		LoginVo loginVo = new LoginVo();
		if (loginRequest.getUserId() != null)
			loginVo.setUserId(loginRequest.getUserId().trim());
		if (loginRequest.getPassword() != null)
			loginVo.setPassword(loginRequest.getPassword().trim());
		return loginVo;
	}
	
	public RegistrationVo convertRegistrationVoFromRegistrationRequest(RegisterationRequest registerationRequest) {
		RegistrationVo registrationVo = new RegistrationVo();
		if (registerationRequest.getEmailId() != null)
			registrationVo.setEmailId(registerationRequest.getEmailId().trim());
		if (registerationRequest.getFirstName() != null)
			registrationVo.setFirstName(registerationRequest.getFirstName().trim());
		if (registerationRequest.getFullName() != null)
			registrationVo.setFullName(registerationRequest.getFullName().trim());
		if (registerationRequest.getLastName() != null)
			registrationVo.setLastName(registerationRequest.getLastName().trim());
		if (registerationRequest.getMobileNo() != null)
			registrationVo.setMobileNo(registerationRequest.getMobileNo().trim());
		if (registerationRequest.getPassword() != null)
			registrationVo.setPassword(registerationRequest.getPassword().trim());
		if (registerationRequest.getPhoneNo() != null)
			registrationVo.setPhoneNo(registerationRequest.getPhoneNo().trim());
		registrationVo.setSubscriptionId(registerationRequest.getSubscriptionId());
		if (registerationRequest.getUserId() != null)
			registrationVo.setUserId(registerationRequest.getUserId().trim());
		registrationVo.setIndividual(registerationRequest.isIndividual());
		registrationVo.setOrganization(registerationRequest.isOrganization());
		return registrationVo;
	}
	
	public RecoverPasswordVo convertRecoverPasswordVoFromRecoverPasswordRequest(
			RecoverPasswordRequest recoverPasswordRequest) {
		RecoverPasswordVo recoverPasswordVo = new RecoverPasswordVo();
		recoverPasswordVo.setEmailId(recoverPasswordRequest.getEmailId());
		return recoverPasswordVo;
	}

	public ResetPasswordVo convertResetPasswordVoFromResetPasswordRequest(ResetPasswordRequest resetPasswordRequest) {
		ResetPasswordVo resetPasswordVo = new ResetPasswordVo();
		resetPasswordVo.setResetToken(resetPasswordRequest.getToken());
		resetPasswordVo.setPassword(resetPasswordRequest.getPassword());
		return resetPasswordVo;
	}
	
	public NewOrganizationVo convertOrganizationVoFromOrganizationRequest(NewOrganizationRequest organizationRequest) {
		NewOrganizationVo data = new NewOrganizationVo();
		OrganizationGeneralInfoVo organizationGeneralInfo = new OrganizationGeneralInfoVo();
		GSTDetailsVo gstDetails = new GSTDetailsVo();
		List<LocationVo> locationList = new ArrayList<LocationVo>();
		data.setGeneralInfo(organizationGeneralInfo);
		data.setGstDetails(gstDetails);
		data.getGstDetails().setLocation(locationList);
		data.setId(organizationRequest.getId());
		data.setStatus(organizationRequest.getStatus());
		data.getGeneralInfo().setName(organizationRequest.getGeneralInfo().getName());
		data.getGeneralInfo().setConstitutionId(organizationRequest.getGeneralInfo().getConstitutionId());
		data.getGeneralInfo().setCinno(organizationRequest.getGeneralInfo().getCinno());
		data.getGeneralInfo().setOrganizationTypeId(organizationRequest.getGeneralInfo().getOrganizationTypeId());
		data.getGeneralInfo()
		.setIndustryOfOrganization(organizationRequest.getGeneralInfo().getIndustryOfOrganization());
		data.getGeneralInfo().setBaseCurrency(organizationRequest.getGeneralInfo().getBaseCurrency());
		data.getGeneralInfo().setFinancialYear(organizationRequest.getGeneralInfo().getFinancialYear());
		data.getGeneralInfo().setDateFormat(organizationRequest.getGeneralInfo().getDateFormat());
		data.getGeneralInfo().setTimeZone(organizationRequest.getGeneralInfo().getTimeZone());
		data.getGeneralInfo().setContactNumber(organizationRequest.getGeneralInfo().getContactNumber());
		data.getGeneralInfo().setEmailId(organizationRequest.getGeneralInfo().getEmailId());
		data.getGeneralInfo().setIeCodeNo(organizationRequest.getGeneralInfo().getIeCodeNo());
		data.getGeneralInfo().setIsCash(organizationRequest.getGeneralInfo().getIsCash());
		data.setUserId(organizationRequest.getUserId());
		data.setSuperAdmin(organizationRequest.getIsSuperAdmin());

		data.getGstDetails().setId(organizationRequest.getGstDetails().getId());
		data.getGstDetails().setGstRegnTypeId(organizationRequest.getGstDetails().getGstRegnTypeId());
		data.getGstDetails().setGstNo(organizationRequest.getGstDetails().getGstNo());
		data.getGstDetails().setTaxPanId(organizationRequest.getGstDetails().getTaxPanId());
		data.getGstDetails().setCity(organizationRequest.getGstDetails().getCity());
		data.getGstDetails().setState(organizationRequest.getGstDetails().getState());
		data.getGstDetails().setCountry(organizationRequest.getGstDetails().getCountry());
		data.getGstDetails().setAddress_1(organizationRequest.getGstDetails().getAddress_1());
		data.getGstDetails().setAddress_2(organizationRequest.getGstDetails().getAddress_2());
		data.getGstDetails().setPinCode(organizationRequest.getGstDetails().getPinCode());
		data.getGstDetails().setIsMultiGST(organizationRequest.getGstDetails().getIsMultiGST());
		boolean isMultiGST = data.getGstDetails().getIsMultiGST();

		if (organizationRequest.getGstDetails().getLocation() != null
				&& organizationRequest.getGstDetails().getLocation().size() > 0) {
			List<LocationVo> locationVoList = new ArrayList<LocationVo>();
			for (int i = 0; i < organizationRequest.getGstDetails().getLocation().size(); i++) {
				LocationRequest locationRequest = organizationRequest.getGstDetails().getLocation().get(i);
				LocationVo locationVo = new LocationVo();
				locationVo.setId(locationRequest.getId());
				locationVo.setName(locationRequest.getName());
				if (isMultiGST) {
					locationVo.setGstNo(locationRequest.getGstNo());
					locationVo.setTaxPanId(locationRequest.getTaxPanId());
				}
				locationVo.setAddress_1(locationRequest.getAddress_1());
				locationVo.setAddress_2(locationRequest.getAddress_2());
				locationVo.setCity(locationRequest.getCity());
				locationVo.setState(locationRequest.getState());
				locationVo.setCountry(locationRequest.getCountry());
				locationVo.setPinCode(locationRequest.getPinCode());
				locationVo.setStatus(locationRequest.getStatus());
				locationVoList.add(locationVo);
			}
			data.getGstDetails().setLocation(locationVoList);
		}
		KeyMembersVo keyMembersVo = new KeyMembersVo();
		data.setKeyMembers(keyMembersVo);

		if (organizationRequest.getKeyMembers().getOrganizationProprietor() != null) {

			if (organizationRequest.getKeyMembers().getOrganizationProprietor() != null
					&& organizationRequest.getKeyMembers().getOrganizationProprietor().size() > 0) {
				List<OrganizationProprietorVo> organizationProprietorList = new ArrayList<OrganizationProprietorVo>();
				for (int i = 0; i < organizationRequest.getKeyMembers().getOrganizationProprietor().size(); i++) {
					OrganizationProprietorRequest organizationProprietorRequest = organizationRequest.getKeyMembers()
							.getOrganizationProprietor().get(i);
					OrganizationProprietorVo organizationProprietorVo = new OrganizationProprietorVo();
					organizationProprietorVo.setName(organizationProprietorRequest.getName());
					organizationProprietorVo.setEmailId(organizationProprietorRequest.getEmailId());
					organizationProprietorVo.setMobileNo(organizationProprietorRequest.getMobileNo());
					organizationProprietorVo.setStatus(organizationProprietorRequest.getStatus());
					organizationProprietorVo.setId(organizationProprietorRequest.getId());
					organizationProprietorList.add(organizationProprietorVo);
				}
				data.getKeyMembers().setOrganizationProprietor(organizationProprietorList);
			}
		} else if (organizationRequest.getKeyMembers().getOrganizationPartnershipPartner() != null) {

			if (organizationRequest.getKeyMembers().getOrganizationPartnershipPartner() != null
					&& organizationRequest.getKeyMembers().getOrganizationPartnershipPartner().size() > 0) {
				List<OrganizationPartnershipPartnerVo> organizationPartnershipPartnerList = new ArrayList<OrganizationPartnershipPartnerVo>();
				for (int i = 0; i < organizationRequest.getKeyMembers().getOrganizationPartnershipPartner()
						.size(); i++) {
					OrganizationPartnershipPartnerRequest organizationPartnershipPartnerRequest = organizationRequest
							.getKeyMembers().getOrganizationPartnershipPartner().get(i);
					OrganizationPartnershipPartnerVo organizationPartnershipPartnerVo = new OrganizationPartnershipPartnerVo();
					organizationPartnershipPartnerVo.setName(organizationPartnershipPartnerRequest.getName());
					organizationPartnershipPartnerVo.setEmailId(organizationPartnershipPartnerRequest.getEmailId());
					organizationPartnershipPartnerVo.setMobileNo(organizationPartnershipPartnerRequest.getMobileNo());
					organizationPartnershipPartnerVo
					.setOwnershipPercentage(organizationPartnershipPartnerRequest.getOwnershipPercentage());
					organizationPartnershipPartnerVo.setStatus(organizationPartnershipPartnerRequest.getStatus());
					organizationPartnershipPartnerVo.setId(organizationPartnershipPartnerRequest.getId());
					organizationPartnershipPartnerList.add(organizationPartnershipPartnerVo);
				}
				data.getKeyMembers().setOrganizationPartnershipPartner(organizationPartnershipPartnerList);
			}

		} else if (organizationRequest.getKeyMembers().getOrganizationPrivateLimitedDirector() != null) {
			if (organizationRequest.getKeyMembers().getOrganizationPrivateLimitedDirector() != null
					&& organizationRequest.getKeyMembers().getOrganizationPrivateLimitedDirector().size() > 0) {
				List<OrganizationPrivateLimitedDirectorVo> organizationPrivateLimitedDirectorList = new ArrayList<OrganizationPrivateLimitedDirectorVo>();
				for (int i = 0; i < organizationRequest.getKeyMembers().getOrganizationPrivateLimitedDirector()
						.size(); i++) {
					OrganizationPrivateLimitedDirectorRequest organizationPrivateLimitedDirectorRequest = organizationRequest
							.getKeyMembers().getOrganizationPrivateLimitedDirector().get(i);
					OrganizationPrivateLimitedDirectorVo organizationPrivateLimitedDirectorVo = new OrganizationPrivateLimitedDirectorVo();
					organizationPrivateLimitedDirectorVo.setName(organizationPrivateLimitedDirectorRequest.getName());
					organizationPrivateLimitedDirectorVo
					.setEmailId(organizationPrivateLimitedDirectorRequest.getEmailId());
					organizationPrivateLimitedDirectorVo
					.setMobileNo(organizationPrivateLimitedDirectorRequest.getMobileNo());
					organizationPrivateLimitedDirectorVo
					.setOwnershipPercentage(organizationPrivateLimitedDirectorRequest.getOwnershipPercentage());
					organizationPrivateLimitedDirectorVo.setDin(organizationPrivateLimitedDirectorRequest.getDin());
					organizationPrivateLimitedDirectorVo
					.setStatus(organizationPrivateLimitedDirectorRequest.getStatus());
					organizationPrivateLimitedDirectorVo.setId(organizationPrivateLimitedDirectorRequest.getId());
					organizationPrivateLimitedDirectorList.add(organizationPrivateLimitedDirectorVo);
				}
				data.getKeyMembers().setOrganizationPrivateLimitedDirector(organizationPrivateLimitedDirectorList);
			}

		} else if (organizationRequest.getKeyMembers().getOrganizationOnePersonDirector() != null) {

			if (organizationRequest.getKeyMembers().getOrganizationOnePersonDirector() != null
					&& organizationRequest.getKeyMembers().getOrganizationOnePersonDirector().size() > 0) {
				List<OrganizationOnePersonDirectorVo> organizationOnePersonDirectorList = new ArrayList<OrganizationOnePersonDirectorVo>();
				for (int i = 0; i < organizationRequest.getKeyMembers().getOrganizationOnePersonDirector()
						.size(); i++) {
					OrganizationOnePersonDirectorRequest organizationOnePersonDirectorRequest = organizationRequest
							.getKeyMembers().getOrganizationOnePersonDirector().get(i);
					OrganizationOnePersonDirectorVo organizationOnePersonDirectorVo = new OrganizationOnePersonDirectorVo();
					organizationOnePersonDirectorVo.setName(organizationOnePersonDirectorRequest.getName());
					organizationOnePersonDirectorVo.setEmailId(organizationOnePersonDirectorRequest.getEmailId());
					organizationOnePersonDirectorVo.setMobileNo(organizationOnePersonDirectorRequest.getMobileNo());
					organizationOnePersonDirectorVo.setDin(organizationOnePersonDirectorRequest.getDin());
					organizationOnePersonDirectorVo.setStatus(organizationOnePersonDirectorRequest.getStatus());
					organizationOnePersonDirectorVo.setId(organizationOnePersonDirectorRequest.getId());
					organizationOnePersonDirectorList.add(organizationOnePersonDirectorVo);
				}
				data.getKeyMembers().setOrganizationOnePersonDirector(organizationOnePersonDirectorList);
			}

		} else if (organizationRequest.getKeyMembers().getOrganizationLimitedLiabilityPartner() != null) {

			if (organizationRequest.getKeyMembers().getOrganizationLimitedLiabilityPartner() != null
					&& organizationRequest.getKeyMembers().getOrganizationLimitedLiabilityPartner().size() > 0) {
				List<OrganizationLimitedLiabilityPartnerVo> organizationLimitedLiabilityPartnerVoList = new ArrayList<OrganizationLimitedLiabilityPartnerVo>();
				for (int i = 0; i < organizationRequest.getKeyMembers().getOrganizationLimitedLiabilityPartner()
						.size(); i++) {
					OrganizationLimitedLiabilityPartnerRequest organizationLimitedLiabilityPartnerRequest = organizationRequest
							.getKeyMembers().getOrganizationLimitedLiabilityPartner().get(i);
					OrganizationLimitedLiabilityPartnerVo organizationLimitedLiabilityPartnerVo = new OrganizationLimitedLiabilityPartnerVo();
					organizationLimitedLiabilityPartnerVo.setName(organizationLimitedLiabilityPartnerRequest.getName());
					organizationLimitedLiabilityPartnerVo
					.setEmailId(organizationLimitedLiabilityPartnerRequest.getEmailId());
					organizationLimitedLiabilityPartnerVo
					.setMobileNo(organizationLimitedLiabilityPartnerRequest.getMobileNo());
					organizationLimitedLiabilityPartnerVo.setOwnershipPercentage(
							organizationLimitedLiabilityPartnerRequest.getOwnershipPercentage());
					organizationLimitedLiabilityPartnerVo
					.setStatus(organizationLimitedLiabilityPartnerRequest.getStatus());
					organizationLimitedLiabilityPartnerVo.setId(organizationLimitedLiabilityPartnerRequest.getId());
					organizationLimitedLiabilityPartnerVoList.add(organizationLimitedLiabilityPartnerVo);
				}
				data.getKeyMembers().setOrganizationLimitedLiabilityPartner(organizationLimitedLiabilityPartnerVoList);
			}

		} else if (organizationRequest.getKeyMembers().getOrganizationPublicLimitedDirector() != null) {

			if (organizationRequest.getKeyMembers().getOrganizationPublicLimitedDirector() != null
					&& organizationRequest.getKeyMembers().getOrganizationPublicLimitedDirector().size() > 0) {
				List<OrganizationPublicLimitedDirectorVo> organizationPublicLimitedDirectorVoList = new ArrayList<OrganizationPublicLimitedDirectorVo>();
				for (int i = 0; i < organizationRequest.getKeyMembers().getOrganizationPublicLimitedDirector()
						.size(); i++) {
					OrganizationPublicLimitedDirectorRequest organizationPublicLimitedDirectorRequest = organizationRequest
							.getKeyMembers().getOrganizationPublicLimitedDirector().get(i);
					OrganizationPublicLimitedDirectorVo organizationPublicLimitedDirectorVo = new OrganizationPublicLimitedDirectorVo();
					organizationPublicLimitedDirectorVo.setName(organizationPublicLimitedDirectorRequest.getName());
					organizationPublicLimitedDirectorVo
					.setEmailId(organizationPublicLimitedDirectorRequest.getEmailId());
					organizationPublicLimitedDirectorVo
					.setMobileNo(organizationPublicLimitedDirectorRequest.getMobileNo());
					organizationPublicLimitedDirectorVo
					.setOwnershipPercentage(organizationPublicLimitedDirectorRequest.getOwnershipPercentage());
					organizationPublicLimitedDirectorVo.setDin(organizationPublicLimitedDirectorRequest.getDin());
					organizationPublicLimitedDirectorVo.setStatus(organizationPublicLimitedDirectorRequest.getStatus());
					organizationPublicLimitedDirectorVo.setId(organizationPublicLimitedDirectorRequest.getId());
					organizationPublicLimitedDirectorVoList.add(organizationPublicLimitedDirectorVo);
				}
				data.getKeyMembers().setOrganizationPublicLimitedDirector(organizationPublicLimitedDirectorVoList);
			}

		}
		List<UploadFileVo> uploadList = new ArrayList<UploadFileVo>();
		if (organizationRequest.getAttachments() != null && organizationRequest.getAttachments().size() > 0) {
			organizationRequest.getAttachments().forEach(file -> {
				uploadList.add(ConvertToVoHelper.getInstance().convertAttachmentFromReq(file));
			});
		}

		data.setAttachments(uploadList);
		data.setAttachmentsToRemove(organizationRequest.getAttachmentsToRemove());
		return data;

	}

	public MinimalOrganizationVo convertMinimalOrganizationVoFromMinimalOrganizationRequest(MinimalOrganizationRequest data){
		MinimalOrganizationVo voData=new MinimalOrganizationVo();	
		voData.setId(data.getId());
		voData.setStatus(data.getStatus());
		voData.setPan(data.getPan());
		voData.setConstitutionId(data.getConstitutionId());
		voData.setGstRegnTypeId(data.getGstRegnTypeId());
		voData.setGst(data.getGst());
		voData.setLegalName(data.getLegalName());
		voData.setDisplayName(data.getDisplayName());
		voData.setOrganizationTypeId(data.getOrganizationTypeId());
		voData.setIndustryOfOrganization(data.getIndustryOfOrganization());
		voData.setUserId(data.getUserId());
		voData.setUserProfile(data.getUserProfile());
		voData.setBrandName(data.getBrandName());	
		voData.setOtherGsts(data.getOtherGsts());
		return voData;
	}

	public RoleVo convertRoleVoFromRoleRequest(RoleRequest roleRequest) {
		RoleVo roleVo = new RoleVo();
		roleVo.setRoleName(roleRequest.getRoleName());
		roleVo.setUserId(roleRequest.getUserId());
		if (roleRequest.getName() != null)
			roleVo.setName(roleRequest.getName().trim());
		if (roleRequest.getDescription() != null)
			roleVo.setDescription(roleRequest.getDescription().trim());
		roleVo.setStatus(roleRequest.getStatus());
		if ((roleRequest.getStatus() == null)
				|| (roleRequest.getStatus() != null && roleRequest.getStatus().equals("INA"))) {
			roleVo.setStatus("ACT");
		}
		roleVo.setOrganizationId(roleRequest.getOrganizationId());
		roleVo.setId(roleRequest.getId());
		roleVo.setAccessData(roleRequest.getAccessData());
		return roleVo;
	}
	
	public UserVo convertUserVoFromUserRequest(UserRequest userRequest) {
		UserVo userVo = new UserVo();
		userVo.setRoleName(userRequest.getRoleName());
		userVo.setUserId(userRequest.getUserId());
		if (userRequest.getFirstName() != null)
			userVo.setFirstName(userRequest.getFirstName().trim());
		if (userRequest.getLastName() != null)
			userVo.setLastName(userRequest.getLastName().trim());
		if (userRequest.getEmailId() != null)
			userVo.setEmailId(userRequest.getEmailId().trim());
		if (userRequest.getPhoneNo() != null)
			userVo.setPhoneNo(userRequest.getPhoneNo().trim());
		userVo.setRoleId(userRequest.getRoleId());
		if (userRequest.getDob() != null)
			userVo.setDob(userRequest.getDob().trim());
		userVo.setStatus(userRequest.getStatus());
		if (userRequest.getGender() != null)
			userVo.setGender(userRequest.getGender().trim());
		userVo.setId(userRequest.getId());
		userVo.setOrganizationId(userRequest.getOrganizationId());
		if (userRequest.getRegisteredAddress() != null) {
			RegisteredAddressVo registeredAddressVo = new RegisteredAddressVo();
			if (userRequest.getRegisteredAddress().getAddressLine1() != null)
				registeredAddressVo.setAddressLine1(userRequest.getRegisteredAddress().getAddressLine1().trim());
			if (userRequest.getRegisteredAddress().getAddressLine2() != null)
				registeredAddressVo.setAddressLine2(userRequest.getRegisteredAddress().getAddressLine2().trim());
			if (userRequest.getRegisteredAddress().getPinCode() != null)
				registeredAddressVo.setPinCode(userRequest.getRegisteredAddress().getPinCode().trim());
			registeredAddressVo.setCountry(userRequest.getRegisteredAddress().getCountry());
			if (userRequest.getRegisteredAddress().getCity() != null)
				registeredAddressVo.setCity(userRequest.getRegisteredAddress().getCity().trim());
			registeredAddressVo.setState(userRequest.getRegisteredAddress().getState());
			registeredAddressVo.setId(userRequest.getRegisteredAddress().getId());
			userVo.setRegisteredAddress(registeredAddressVo);
		}
		if ((userRequest.getStatus() == null)
				|| (userRequest.getStatus() != null && userRequest.getStatus().equals("INA"))) {
			userVo.setStatus("ACT");
		}
		userVo.setAccessData(userRequest.getAccessData());
		return userVo;

	}
	
	public InviteUserVo convertInviteUserVoFromInviteUserRequest(InviteUserRequest request) {
		InviteUserVo invite = new InviteUserVo();
		invite.setOrgId(request.getOrgId());
		invite.setRoleName(request.getRoleName());
		invite.setStatus(request.getStatus());
		invite.setUserId(request.getUserId());
		invite.setUpdateRoleName(request.getUpdateRoleName());
		invite.setUpdateUserId(request.getUpdateUserId());
		List<RoleUserVo> roleUser=new ArrayList<RoleUserVo>();
		for(int i=0;i<request.getRoleUser().size();i++){
			RoleUserRequest roleUserRequest=request.getRoleUser().get(i);
			RoleUserVo roleUserVo=new RoleUserVo();
			roleUserVo.setEmailIdList(roleUserRequest.getEmailIdList());
			roleUserVo.setMessage(roleUserRequest.getMessage());
			roleUserVo.setRoleId(roleUserRequest.getRoleId());	
			roleUser.add(roleUserVo);
		}
		invite.setRoleUser(roleUser);

		return invite;
	}
	
	public InviteActionVo convertInviteActionVoFromInviteActionRequest(InviteActionRequest request) {
		InviteActionVo action = new InviteActionVo();
		action.setId(request.getId());
		action.setAction(request.getAction());
		action.setReason(request.getReason());
		/*action.setUserId(request.getUserId());
		request.setRoleName(request.getRoleName());*/
		return action;
	}
	
	public ProfileVo convertProfileVoFromProfileRequest(ProfileRequest request) {
		ProfileVo profile = new ProfileVo();
		profile.setEmailId(request.getEmailId());
		profile.setFirstName(request.getFirstName());
		profile.setLastName(request.getLastName());
		profile.setMobileNo(request.getMobileNo());
		profile.setRoleName(request.getRoleName());
		profile.setProfilePic(request.getProfilePic());
		return profile;
	}
	
	public ChangePasswordVo convertChangePasswordVoFromChangePasswordRequest(ChangePasswordRequest request) {
		ChangePasswordVo vo = new ChangePasswordVo();
		vo.setEmail(request.getEmail());
		vo.setNewPassword(request.getNewPassword());
		vo.setOldPassword(request.getOldPassword());
		return vo;
	}
}
