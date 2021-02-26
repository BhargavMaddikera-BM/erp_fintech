package com.blackstrawai.export;

import java.util.List;

public class CustomerExportVo {

	private Integer id;
	private String primaryContact;
	private String companyName;
	private String customerDisplayName;
	private String email;
	private String phone;
	private String mobileNo;
	private String website;
	private String pan;
	private String organizationType;
	private String customerGroup;
	private String gstRegnType;
	private String gstNumber;
	private String currency;
	private String openingBalance;
	private String paymentTerms;
	private String deliveryAddrAttention;
	private String deliveryAddrPhone;
	private String deliveryAddrCountry;
	private String deliveryAddrLine1;
	private String deliveryAddrLine2;
	private String deliveryAddrLandmark;
	private String deliveryAddrState;
	private String deliveryAddrCity;
	private String deliveryAddrPinCode;
	private String billingAddrAttention;
	private String billingAddrPhone;
	private String billingAddrCountry;
	private String billingAddrLine1;
	private String billingAddrLine2;
	private String billingAddrLandmark;
	private String billingAddrState;
	private String billingAddrCity;
	private String billingAddrPinCode;
	private List<BankExportVo> bankDetails;
	private List<ContactsExportVo> contacts;
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPrimaryContact() {
		return primaryContact;
	}

	public void setPrimaryContact(String primaryContact) {
		this.primaryContact = primaryContact;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCustomerDisplayName() {
		return customerDisplayName;
	}

	public void setCustomerDisplayName(String customerDisplayName) {
		this.customerDisplayName = customerDisplayName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(String organizationType) {
		this.organizationType = organizationType;
	}

	public String getCustomerGroup() {
		return customerGroup;
	}

	public void setCustomerGroup(String customerGroup) {
		this.customerGroup = customerGroup;
	}

	public String getGstRegnType() {
		return gstRegnType;
	}

	public void setGstRegnType(String gstRegnType) {
		this.gstRegnType = gstRegnType;
	}

	public String getGstNumber() {
		return gstNumber;
	}

	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(String openingBalance) {
		this.openingBalance = openingBalance;
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public String getDeliveryAddrAttention() {
		return deliveryAddrAttention;
	}

	public void setDeliveryAddrAttention(String deliveryAddrAttention) {
		this.deliveryAddrAttention = deliveryAddrAttention;
	}

	public String getDeliveryAddrPhone() {
		return deliveryAddrPhone;
	}

	public void setDeliveryAddrPhone(String deliveryAddrPhone) {
		this.deliveryAddrPhone = deliveryAddrPhone;
	}

	public String getDeliveryAddrCountry() {
		return deliveryAddrCountry;
	}

	public void setDeliveryAddrCountry(String deliveryAddrCountry) {
		this.deliveryAddrCountry = deliveryAddrCountry;
	}

	public String getDeliveryAddrLine1() {
		return deliveryAddrLine1;
	}

	public void setDeliveryAddrLine1(String deliveryAddrLine1) {
		this.deliveryAddrLine1 = deliveryAddrLine1;
	}

	public String getDeliveryAddrLine2() {
		return deliveryAddrLine2;
	}

	public void setDeliveryAddrLine2(String deliveryAddrLine2) {
		this.deliveryAddrLine2 = deliveryAddrLine2;
	}

	public String getDeliveryAddrLandmark() {
		return deliveryAddrLandmark;
	}

	public void setDeliveryAddrLandmark(String deliveryAddrLandmark) {
		this.deliveryAddrLandmark = deliveryAddrLandmark;
	}

	public String getDeliveryAddrState() {
		return deliveryAddrState;
	}

	public void setDeliveryAddrState(String deliveryAddrState) {
		this.deliveryAddrState = deliveryAddrState;
	}

	public String getDeliveryAddrCity() {
		return deliveryAddrCity;
	}

	public void setDeliveryAddrCity(String deliveryAddrCity) {
		this.deliveryAddrCity = deliveryAddrCity;
	}

	public String getDeliveryAddrPinCode() {
		return deliveryAddrPinCode;
	}

	public void setDeliveryAddrPinCode(String deliveryAddrPinCode) {
		this.deliveryAddrPinCode = deliveryAddrPinCode;
	}

	public String getBillingAddrAttention() {
		return billingAddrAttention;
	}

	public void setBillingAddrAttention(String billingAddrAttention) {
		this.billingAddrAttention = billingAddrAttention;
	}

	public String getBillingAddrPhone() {
		return billingAddrPhone;
	}

	public void setBillingAddrPhone(String billingAddrPhone) {
		this.billingAddrPhone = billingAddrPhone;
	}

	public String getBillingAddrCountry() {
		return billingAddrCountry;
	}

	public void setBillingAddrCountry(String billingAddrCountry) {
		this.billingAddrCountry = billingAddrCountry;
	}

	public String getBillingAddrLine1() {
		return billingAddrLine1;
	}

	public void setBillingAddrLine1(String billingAddrLine1) {
		this.billingAddrLine1 = billingAddrLine1;
	}

	public String getBillingAddrLine2() {
		return billingAddrLine2;
	}

	public void setBillingAddrLine2(String billingAddrLine2) {
		this.billingAddrLine2 = billingAddrLine2;
	}

	public String getBillingAddrLandmark() {
		return billingAddrLandmark;
	}

	public void setBillingAddrLandmark(String billingAddrLandmark) {
		this.billingAddrLandmark = billingAddrLandmark;
	}

	public String getBillingAddrState() {
		return billingAddrState;
	}

	public void setBillingAddrState(String billingAddrState) {
		this.billingAddrState = billingAddrState;
	}

	public String getBillingAddrCity() {
		return billingAddrCity;
	}

	public void setBillingAddrCity(String billingAddrCity) {
		this.billingAddrCity = billingAddrCity;
	}

	public String getBillingAddrPinCode() {
		return billingAddrPinCode;
	}

	public void setBillingAddrPinCode(String billingAddrPinCode) {
		this.billingAddrPinCode = billingAddrPinCode;
	}

	public List<ContactsExportVo> getContacts() {
		return contacts;
	}

	public void setContacts(List<ContactsExportVo> contacts) {
		this.contacts = contacts;
	}

	public List<BankExportVo> getBankDetails() {
		return bankDetails;
	}

	public void setBankDetails(List<BankExportVo> bankDetails) {
		this.bankDetails = bankDetails;
	}

}
