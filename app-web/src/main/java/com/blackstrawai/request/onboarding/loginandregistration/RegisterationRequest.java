package com.blackstrawai.request.onboarding.loginandregistration;

import com.blackstrawai.common.BaseRequest;

public class RegisterationRequest extends BaseRequest{
	
	  private String firstName;
	  private String lastName;
	  private String fullName;
	  private String mobileNo;
	  private String phoneNo;
	  private int subscriptionId;
	  private String password;
	  private String emailId;
	  private boolean isIndividual;
	  private boolean isOrganization;

	public boolean isIndividual() {
		return isIndividual;
	}
	public void setIndividual(boolean isIndividual) {
		this.isIndividual = isIndividual;
	}
	public boolean isOrganization() {
		return isOrganization;
	}
	public void setOrganization(boolean isOrganization) {
		this.isOrganization = isOrganization;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public int getSubscriptionId() {
		return subscriptionId;
	}
	public void setSubscriptionId(int subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	  

}
