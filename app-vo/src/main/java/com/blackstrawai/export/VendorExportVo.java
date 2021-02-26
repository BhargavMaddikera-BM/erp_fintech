package com.blackstrawai.export;

import java.util.List;

public class VendorExportVo {

	private Integer id;
	private String primaryContact;
	private String companyName;
	private String vendorDisplayName;
	private String email;
	private String phone;
	private String mobileNo;
	private String pan;
	private String organizationType;
	private String vendorGroup;
	private String gstRegnType;
	private String gstNumber;
	private String currency;
	private String openingBalance;
	private String paymentTerms;
	private String tds;
	private String originAddrAttention;
	private String originAddrPhone;
	private String originAddrCountry;
	private String originAddrLine1;
	private String originAddrLine2;
	private String originAddrLandmark;
	private String originAddrState;
	private String originAddrCity;
	private String originAddrPinCode;
	private String billingAddrAttention;
	private String billingAddrPhone;
	private String billingAddrCountry;
	private String billingAddrLine1;
	private String billingAddrLine2;
	private String billingAddrLandmark;
	private String billingAddrState;
	private String billingAddrCity;
	private String billingAddrPinCode;
	private String status;
	private List<BankExportVo> bankDetails;
	private List<ContactsExportVo> contacts;

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

	public String getVendorDisplayName() {
		return vendorDisplayName;
	}

	public void setVendorDisplayName(String vendorDisplayName) {
		this.vendorDisplayName = vendorDisplayName;
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

	public String getVendorGroup() {
		return vendorGroup;
	}

	public void setVendorGroup(String vendorGroup) {
		this.vendorGroup = vendorGroup;
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

	public String getTds() {
		return tds;
	}

	public void setTds(String tds) {
		this.tds = tds;
	}

	public String getOriginAddrAttention() {
		return originAddrAttention;
	}

	public void setOriginAddrAttention(String originAddrAttention) {
		this.originAddrAttention = originAddrAttention;
	}

	public String getOriginAddrPhone() {
		return originAddrPhone;
	}

	public void setOriginAddrPhone(String originAddrPhone) {
		this.originAddrPhone = originAddrPhone;
	}

	public String getOriginAddrCountry() {
		return originAddrCountry;
	}

	public void setOriginAddrCountry(String originAddrCountry) {
		this.originAddrCountry = originAddrCountry;
	}

	public String getOriginAddrLine1() {
		return originAddrLine1;
	}

	public void setOriginAddrLine1(String originAddrLine1) {
		this.originAddrLine1 = originAddrLine1;
	}

	public String getOriginAddrLine2() {
		return originAddrLine2;
	}

	public void setOriginAddrLine2(String originAddrLine2) {
		this.originAddrLine2 = originAddrLine2;
	}

	public String getOriginAddrLandmark() {
		return originAddrLandmark;
	}

	public void setOriginAddrLandmark(String originAddrLandmark) {
		this.originAddrLandmark = originAddrLandmark;
	}

	public String getOriginAddrState() {
		return originAddrState;
	}

	public void setOriginAddrState(String originAddrState) {
		this.originAddrState = originAddrState;
	}

	public String getOriginAddrCity() {
		return originAddrCity;
	}

	public void setOriginAddrCity(String originAddrCity) {
		this.originAddrCity = originAddrCity;
	}

	public String getOriginAddrPinCode() {
		return originAddrPinCode;
	}

	public void setOriginAddrPinCode(String originAddrPinCode) {
		this.originAddrPinCode = originAddrPinCode;
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

	public List<BankExportVo> getBankDetails() {
		return bankDetails;
	}

	public void setBankDetails(List<BankExportVo> bankDetails) {
		this.bankDetails = bankDetails;
	}

	public List<ContactsExportVo> getContacts() {
		return contacts;
	}

	public void setContacts(List<ContactsExportVo> contacts) {
		this.contacts = contacts;
	}

}
