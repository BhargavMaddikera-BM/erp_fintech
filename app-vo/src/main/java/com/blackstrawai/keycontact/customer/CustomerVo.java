package com.blackstrawai.keycontact.customer;

import java.util.List;

import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseAddressVo;
import com.blackstrawai.common.BaseVo;
import com.blackstrawai.common.BookKeepingSettingsVo;

public class CustomerVo extends BaseVo {

	private Integer id;

	private String status;

	private CustomerPrimaryInfoVo primaryInfo;

	private CustomerBillingAddressVo billingAddress;

	private CustomerDeliveryAddressVo deliveryAddress;

	private List<CustomerContactsVo> contacts;

	private List<CustomerBankDetailsVo> bankDetails;

	private List<UploadFileVo> attachments;

	private List<Integer> attachmentsToRemove;

	private Integer organizationId;

	private Boolean isSuperAdmin;
	
	private String roleName;
	
	private List<BaseAddressVo> customerAdditionalAddresses;

	private BookKeepingSettingsVo customerBookKeepingSetting;
	
	private CustomerFinanceVo customerFinance;
	
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

	public CustomerPrimaryInfoVo getPrimaryInfo() {
		return primaryInfo;
	}

	public void setPrimaryInfo(CustomerPrimaryInfoVo primaryInfo) {
		this.primaryInfo = primaryInfo;
	}

	public CustomerBillingAddressVo getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(CustomerBillingAddressVo billingAddress) {
		this.billingAddress = billingAddress;
	}

	public CustomerDeliveryAddressVo getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(CustomerDeliveryAddressVo deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public List<CustomerContactsVo> getContacts() {
		return contacts;
	}

	public void setContacts(List<CustomerContactsVo> contacts) {
		this.contacts = contacts;
	}

	public List<CustomerBankDetailsVo> getBankDetails() {
		return bankDetails;
	}

	public void setBankDetails(List<CustomerBankDetailsVo> bankDetails) {
		this.bankDetails = bankDetails;
	}

	public List<UploadFileVo> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<UploadFileVo> attachments) {
		this.attachments = attachments;
	}

	public List<BaseAddressVo> getCustomerAdditionalAddresses() {
		return customerAdditionalAddresses;
	}

	public void setCustomerAdditionalAddresses(List<BaseAddressVo> customerAdditionalAddresses) {
		this.customerAdditionalAddresses = customerAdditionalAddresses;
	}


	public BookKeepingSettingsVo getCustomerBookKeepingSetting() {
		return customerBookKeepingSetting;
	}

	public void setCustomerBookKeepingSetting(BookKeepingSettingsVo customerBookKeepingSetting) {
		this.customerBookKeepingSetting = customerBookKeepingSetting;
	}


	public CustomerFinanceVo getCustomerFinance() {
		return customerFinance;
	}

	public void setCustomerFinance(CustomerFinanceVo customerFinance) {
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

	@Override
	public String toString() {
		return "CustomerVo [id=" + id + ", status=" + status + ", primaryInfo=" + primaryInfo + ", billingAddress="
				+ billingAddress + ", deliveryAddress=" + deliveryAddress + ", contacts=" + contacts + ", bankDetails="
				+ bankDetails + ", attachments=" + attachments + ", attachmentsToRemove=" + attachmentsToRemove
				+ ", organizationId=" + organizationId + ", isSuperAdmin=" + isSuperAdmin + ", roleName=" + roleName
				+ ", customerAdditionalAddresses=" + customerAdditionalAddresses + ", customerBookKeepingSetting="
				+ customerBookKeepingSetting + ", customerFinance=" + customerFinance + ", defaultGlTallyId="
				+ defaultGlTallyId + ", defaultGlTallyName=" + defaultGlTallyName + "]";
	}




}
