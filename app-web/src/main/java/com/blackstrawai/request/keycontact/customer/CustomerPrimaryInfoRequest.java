package com.blackstrawai.request.keycontact.customer;

public class CustomerPrimaryInfoRequest {

	private String primaryContact;
	
	private String companyName;
	
	private String[] otherGsts;
	
	private String customerDisplayName;
	
	private String emailAddress;
	
	private String phoneNo;
	
	private String websiteAddress;
	
	private String mobileNo;
	
	private String panNo;

	private Integer organisationTypeId;
	
	private Integer customerGroupId;
	
	private Integer gstRegnTypeId;
	
	private String gstNumber;
	
	private Integer currencyId;
	
	private Double openingBal;
	
	private Integer paymentTermsId;
	

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

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getWebsiteAddress() {
		return websiteAddress;
	}

	public void setWebsiteAddress(String websiteAddress) {
		this.websiteAddress = websiteAddress;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	public Integer getOrganisationTypeId() {
		return organisationTypeId;
	}

	public void setOrganisationTypeId(Integer organisationTypeId) {
		this.organisationTypeId = organisationTypeId;
	}

	public Integer getCustomerGroupId() {
		return customerGroupId;
	}

	public void setCustomerGroupId(Integer customerGroupId) {
		this.customerGroupId = customerGroupId;
	}

	public Integer getGstRegnTypeId() {
		return gstRegnTypeId;
	}

	public void setGstRegnTypeId(Integer gstRegnTypeId) {
		this.gstRegnTypeId = gstRegnTypeId;
	}

	public String getGstNumber() {
		return gstNumber;
	}

	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Double getOpeningBal() {
		return openingBal;
	}

	public void setOpeningBal(Double openingBal) {
		this.openingBal = openingBal;
	}

	public Integer getPaymentTermsId() {
		return paymentTermsId;
	}

	public void setPaymentTermsId(Integer paymentTermsId) {
		this.paymentTermsId = paymentTermsId;
	}


	public String[] getOtherGsts() {
		return otherGsts;
	}

	public void setOtherGsts(String[] otherGsts) {
		this.otherGsts = otherGsts;
	}
	
	
	
	
}
