package com.blackstrawai.keycontact.vendor;

import java.util.List;

import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseAddressVo;
import com.blackstrawai.common.BookKeepingSettingsVo;
import com.blackstrawai.common.TokenVo;

public class VendorVo extends TokenVo {

	private Integer vendorId;
	private Integer organizationId;
	private Boolean isSuperAdmin;
	private String status;
	private VendorGeneralInformationVo vendorGeneralInformation;
	private VendorFinanceVo vendorFinance;
	private VendorOriginAddressVo vendorOriginAddress;
	private VendorDestinationAddressVo vendorDestinationAddress;
	private List<BaseAddressVo> vendorAdditionalAddresses;
	private List<VendorContactVo> contacts;
	private List<VendorBankDetailsVo> bankDetails;
	private List<Integer> contactsToRemove;
	private List<Integer>  bankDetailsToRemove;
	private List<UploadFileVo> attachments;
	private List<Integer> attachmentsToRemove;
	private Boolean isVendorOnBoarding;
	private String roleName;
	private BookKeepingSettingsVo vendorBookKeepingSetting;
	private Integer defaultGlTallyId;
	private String defaultGlTallyName;

	private List<VendorBasedOnGstVo> originalAndDestinationAddressBasedOnGst;
	
	

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Boolean getIsVendorOnBoarding() {
		return isVendorOnBoarding;
	}

	public void setIsVendorOnBoarding(Boolean isVendorOnBoarding) {
		this.isVendorOnBoarding = isVendorOnBoarding;
	}

	public List<Integer> getAttachmentsToRemove() {
		return attachmentsToRemove;
	}

	public void setAttachmentsToRemove(List<Integer> attachmentsToRemove) {
		this.attachmentsToRemove = attachmentsToRemove;
	}

	public List<UploadFileVo> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<UploadFileVo> attachments) {
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

	public VendorGeneralInformationVo getVendorGeneralInformation() {
		return vendorGeneralInformation;
	}

	public void setVendorGeneralInformation(VendorGeneralInformationVo vendorGeneralInformation) {
		this.vendorGeneralInformation = vendorGeneralInformation;
	}

	public VendorFinanceVo getVendorFinance() {
		return vendorFinance;
	}

	public void setVendorFinance(VendorFinanceVo vendorFinance) {
		this.vendorFinance = vendorFinance;
	}

	public VendorOriginAddressVo getVendorOriginAddress() {
		return vendorOriginAddress;
	}

	public void setVendorOriginAddress(VendorOriginAddressVo vendorOriginAddress) {
		this.vendorOriginAddress = vendorOriginAddress;
	}

	public VendorDestinationAddressVo getVendorDestinationAddress() {
		return vendorDestinationAddress;
	}

	public void setVendorDestinationAddress(VendorDestinationAddressVo vendorDestinationAddress) {
		this.vendorDestinationAddress = vendorDestinationAddress;
	}

	public List<VendorContactVo> getContacts() {
		return contacts;
	}

	public void setContacts(List<VendorContactVo> contacts) {
		this.contacts = contacts;
	}

	public List<VendorBankDetailsVo> getBankDetails() {
		return bankDetails;
	}

	public void setBankDetails(List<VendorBankDetailsVo> bankDetails) {
		this.bankDetails = bankDetails;
	}

	public BookKeepingSettingsVo getVendorBookKeepingSetting() {
		return vendorBookKeepingSetting;
	}

	public void setVendorBookKeepingSetting(BookKeepingSettingsVo vendorBookKeepingSetting) {
		this.vendorBookKeepingSetting = vendorBookKeepingSetting;
	}

	public List<BaseAddressVo> getVendorAdditionalAddresses() {
		return vendorAdditionalAddresses;
	}

	public void setVendorAdditionalAddresses(List<BaseAddressVo> vendorAdditionalAddresses) {
		this.vendorAdditionalAddresses = vendorAdditionalAddresses;
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


	public List<VendorBasedOnGstVo> getOriginalAndDestinationAddressBasedOnGst() {
		return originalAndDestinationAddressBasedOnGst;
	}

	public void setOriginalAndDestinationAddressBasedOnGst(
			List<VendorBasedOnGstVo> originalAndDestinationAddressBasedOnGst) {
		this.originalAndDestinationAddressBasedOnGst = originalAndDestinationAddressBasedOnGst;
	}


	


}
