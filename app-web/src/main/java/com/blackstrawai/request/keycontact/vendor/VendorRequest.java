package com.blackstrawai.request.keycontact.vendor;

import java.util.List;

import com.blackstrawai.common.BaseRequest;
import com.blackstrawai.request.attachments.UploadFileRequest;
import com.blackstrawai.request.keycontact.customer.BaseAddressRequest;
import com.blackstrawai.request.keycontact.customer.BookKeepingSettingsRequest;

public class VendorRequest extends BaseRequest {

	private Integer vendorId;
	private Integer organizationId;
	private Boolean isSuperAdmin;
	private String status;
	private VendorGeneralInformationRequest vendorGeneralInformation;
	private VendorFinanceRequest vendorFinance;
	private VendorOriginAddressRequest vendorOriginAddress;
	private VendorDestinationAddressRequest vendorDestinationAddress;
	private List<BaseAddressRequest> vendorAdditionalAddresses;
	private BookKeepingSettingsRequest vendorBookKeepingSetting;
	private List<VendorContactRequest> contacts;
	private List<VendorBankDetailsRequest> bankDetails;
	private List<UploadFileRequest> attachments;
	private List<Integer> attachmentsToRemove;
	private Boolean isVendorOnboarding;
	private String roleName;
	private Integer defaultGlTallyId;
	private String defaultGlTallyName;
	private List<Integer> contactsToRemove;
	private List<Integer> bankDetailsToRemove;
	private List<VendorBasedOnGstRequest> originalAndDestinationAddressBasedOnGst;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Boolean getIsVendorOnboarding() {
		return isVendorOnboarding;
	}

	public void setIsVendorOnboarding(Boolean isVendorOnboarding) {
		this.isVendorOnboarding = isVendorOnboarding;
	}

	public List<Integer> getAttachmentsToRemove() {
		return attachmentsToRemove;
	}

	public void setAttachmentsToRemove(List<Integer> attachmentsToRemove) {
		this.attachmentsToRemove = attachmentsToRemove;
	}

	public List<UploadFileRequest> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<UploadFileRequest> attachments) {
		this.attachments = attachments;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}

	public void setIsSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public VendorGeneralInformationRequest getVendorGeneralInformation() {
		return vendorGeneralInformation;
	}

	public void setVendorGeneralInformation(VendorGeneralInformationRequest vendorGeneralInformation) {
		this.vendorGeneralInformation = vendorGeneralInformation;
	}

	public VendorFinanceRequest getVendorFinance() {
		return vendorFinance;
	}

	public void setVendorFinance(VendorFinanceRequest vendorFinance) {
		this.vendorFinance = vendorFinance;
	}

	public VendorOriginAddressRequest getVendorOriginAddress() {
		return vendorOriginAddress;
	}

	public void setVendorOriginAddress(VendorOriginAddressRequest vendorOriginAddress) {
		this.vendorOriginAddress = vendorOriginAddress;
	}

	public VendorDestinationAddressRequest getVendorDestinationAddress() {
		return vendorDestinationAddress;
	}

	public void setVendorDestinationAddress(VendorDestinationAddressRequest vendorDestinationAddress) {
		this.vendorDestinationAddress = vendorDestinationAddress;
	}

	public List<VendorContactRequest> getContacts() {
		return contacts;
	}

	public void setContacts(List<VendorContactRequest> contacts) {
		this.contacts = contacts;
	}

	public List<VendorBankDetailsRequest> getBankDetails() {
		return bankDetails;
	}

	public void setBankDetails(List<VendorBankDetailsRequest> bankDetails) {
		this.bankDetails = bankDetails;
	}

	public List<BaseAddressRequest> getVendorAdditionalAddresses() {
		return vendorAdditionalAddresses;
	}

	public void setVendorAdditionalAddresses(List<BaseAddressRequest> vendorAdditionalAddresses) {
		this.vendorAdditionalAddresses = vendorAdditionalAddresses;
	}

	public BookKeepingSettingsRequest getVendorBookKeepingSetting() {
		return vendorBookKeepingSetting;
	}

	public void setVendorBookKeepingSetting(BookKeepingSettingsRequest vendorBookKeepingSetting) {
		this.vendorBookKeepingSetting = vendorBookKeepingSetting;
	}

	public Integer getDefaultGlTallyId() {
		return defaultGlTallyId;
	}

	public void setDefaultGlTallyId(Integer defaultGlTallyId) {
		this.defaultGlTallyId = defaultGlTallyId;
	}

	public String getDefaultGlTallyName() {
		return defaultGlTallyName;
	}

	public void setDefaultGlTallyName(String defaultGlTallyName) {
		this.defaultGlTallyName = defaultGlTallyName;
	}

	public List<Integer> getContactsToRemove() {
		return contactsToRemove;
	}

	public void setContactsToRemove(List<Integer> contactsToRemove) {
		this.contactsToRemove = contactsToRemove;
	}

	public List<Integer> getBankDetailsToRemove() {
		return bankDetailsToRemove;
	}

	public void setBankDetailsToRemove(List<Integer> bankDetailsToRemove) {
		this.bankDetailsToRemove = bankDetailsToRemove;
	}

	

	public List<VendorBasedOnGstRequest> getOriginalAndDestinationAddressBasedOnGst() {
		return originalAndDestinationAddressBasedOnGst;
	}

	public void setOriginalAndDestinationAddressBasedOnGst(
			List<VendorBasedOnGstRequest> originalAndDestinationAddressBasedOnGst) {
		this.originalAndDestinationAddressBasedOnGst = originalAndDestinationAddressBasedOnGst;
	}

}
