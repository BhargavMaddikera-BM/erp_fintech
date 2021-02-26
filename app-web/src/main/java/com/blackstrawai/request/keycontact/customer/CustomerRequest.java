package com.blackstrawai.request.keycontact.customer;

import java.util.List;

import com.blackstrawai.common.BaseRequest;
import com.blackstrawai.request.attachments.UploadFileRequest;

public class CustomerRequest extends BaseRequest{

	private Integer id;
	
	private String status;
	
	private CustomerPrimaryInfoRequest primaryInfo;
	
	private CustomerBillingAddressRequest billingAddress;
	
	private CustomerDeliveryAddressRequest deliveryAddress;
	
	private List<CustomerContactsRequest> contacts;

	private List<CustomerBankDetailsRequest> bankDetails;
	
	private List<UploadFileRequest> attachments;
	
	private List<Integer> attachmentsToRemove;
	
	private Integer organizationId;
	
	private Boolean isSuperAdmin ;
	
	private String roleName;

	private List<BaseAddressRequest> customerAdditionalAddresses;
	
	private BookKeepingSettingsRequest customerBookKeepingSetting;
	
	private CustomerFinanceRequest customerFinance;
	
	private Integer defaultGlTallyId;
	
	private String defaultGlTallyName;
	
	private List<Integer> contactsToRemove;
	
	private List<Integer>  bankDetailsToRemove;
	
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public CustomerPrimaryInfoRequest getPrimaryInfo() {
		return primaryInfo;
	}

	public void setPrimaryInfo(CustomerPrimaryInfoRequest primaryInfo) {
		this.primaryInfo = primaryInfo;
	}

	public CustomerBillingAddressRequest getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(CustomerBillingAddressRequest billingAddress) {
		this.billingAddress = billingAddress;
	}

	public CustomerDeliveryAddressRequest getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(CustomerDeliveryAddressRequest deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public List<CustomerBankDetailsRequest> getBankDetails() {
		return bankDetails;
	}

	public void setBankDetails(List<CustomerBankDetailsRequest> bankDetails) {
		this.bankDetails = bankDetails;
	}

	public List<UploadFileRequest> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<UploadFileRequest> attachments) {
		this.attachments = attachments;
	}

	
	public List<CustomerContactsRequest> getContacts() {
		return contacts;
	}

	public void setContacts(List<CustomerContactsRequest> contacts) {
		this.contacts = contacts;
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

	public List<Integer> getAttachmentsToRemove() {
		return attachmentsToRemove;
	}

	public void setAttachmentsToRemove(List<Integer> attachmentsToRemove) {
		this.attachmentsToRemove = attachmentsToRemove;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public List<BaseAddressRequest> getCustomerAdditionalAddresses() {
		return customerAdditionalAddresses;
	}

	public void setCustomerAdditionalAddresses(List<BaseAddressRequest> customerAdditionalAddresses) {
		this.customerAdditionalAddresses = customerAdditionalAddresses;
	}

	public BookKeepingSettingsRequest getCustomerBookKeepingSetting() {
		return customerBookKeepingSetting;
	}

	public void setCustomerBookKeepingSetting(BookKeepingSettingsRequest customerBookKeepingSetting) {
		this.customerBookKeepingSetting = customerBookKeepingSetting;
	}

	public CustomerFinanceRequest getCustomerFinance() {
		return customerFinance;
	}

	public void setCustomerFinance(CustomerFinanceRequest customerFinance) {
		this.customerFinance = customerFinance;
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

	
}
