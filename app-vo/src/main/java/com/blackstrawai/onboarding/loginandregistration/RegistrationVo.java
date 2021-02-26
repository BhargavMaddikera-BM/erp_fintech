package com.blackstrawai.onboarding.loginandregistration;

import com.blackstrawai.common.TokenVo;

public class RegistrationVo extends TokenVo{
	
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
	  private String userToken;
	  private String accessData;
	  private String roleName;
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getAccessData() {
		return accessData;
	}
	public void setAccessData(String accessData) {
		this.accessData = accessData;
	}
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	private int id;	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RegistrationVo [firstName=");
		builder.append(firstName);
		builder.append(", lastName=");
		builder.append(lastName);
		builder.append(", fullName=");
		builder.append(fullName);
		builder.append(", mobileNo=");
		builder.append(mobileNo);
		builder.append(", phoneNo=");
		builder.append(phoneNo);
		builder.append(", subscriptionId=");
		builder.append(subscriptionId);
		builder.append(", password=");
		builder.append(password);
		builder.append(", emailId=");
		builder.append(emailId);
		builder.append(", isIndividual=");
		builder.append(isIndividual);
		builder.append(", isOrganization=");
		builder.append(isOrganization);
		builder.append(", userToken=");
		builder.append(userToken);
		builder.append(", id=");
		builder.append(id);
		builder.append("]");
		return builder.toString();
	}

	
	
}
