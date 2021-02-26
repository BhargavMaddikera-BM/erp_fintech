package com.blackstrawai.upload;

public class CustomerUploadVo {

	private String primaryContact;
	private String companyName;
	private String displayName;
	private String email;
	private String mobile;
	private String currency;
	private String deliveryAddressAttention;
	private String deliveryAddressPhoneNo;
	private String deliveryAddressCountry;
	private String deliveryAddressLine1;
	private String deliveryAddressState;
	private String deliveryAddressCity;
	private String deliveryAddressPin;
	private String billingAddressAttention;
	private String billingAddressPhoneNo;
	private String billingAddressCountry;
	private String billingAddressLine1;
	private String billingAddressState;
	private String billingAddressCity;
	private String billingAddressPin;
	private String responseMessage;
	private String responseStatus;

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
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

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getDeliveryAddressAttention() {
		return deliveryAddressAttention;
	}

	public void setDeliveryAddressAttention(String deliveryAddressAttention) {
		this.deliveryAddressAttention = deliveryAddressAttention;
	}

	public String getDeliveryAddressPhoneNo() {
		return deliveryAddressPhoneNo;
	}

	public void setDeliveryAddressPhoneNo(String deliveryAddressPhoneNo) {
		this.deliveryAddressPhoneNo = deliveryAddressPhoneNo;
	}

	public String getDeliveryAddressCountry() {
		return deliveryAddressCountry;
	}

	public void setDeliveryAddressCountry(String deliveryAddressCountry) {
		this.deliveryAddressCountry = deliveryAddressCountry;
	}

	public String getDeliveryAddressLine1() {
		return deliveryAddressLine1;
	}

	public void setDeliveryAddressLine1(String deliveryAddressLine1) {
		this.deliveryAddressLine1 = deliveryAddressLine1;
	}

	public String getDeliveryAddressState() {
		return deliveryAddressState;
	}

	public void setDeliveryAddressState(String deliveryAddressState) {
		this.deliveryAddressState = deliveryAddressState;
	}

	public String getDeliveryAddressCity() {
		return deliveryAddressCity;
	}

	public void setDeliveryAddressCity(String deliveryAddressCity) {
		this.deliveryAddressCity = deliveryAddressCity;
	}

	public String getDeliveryAddressPin() {
		return deliveryAddressPin;
	}

	public void setDeliveryAddressPin(String deliveryAddressPin) {
		this.deliveryAddressPin = deliveryAddressPin;
	}

	public String getBillingAddressAttention() {
		return billingAddressAttention;
	}

	public void setBillingAddressAttention(String billingAddressAttention) {
		this.billingAddressAttention = billingAddressAttention;
	}

	public String getBillingAddressPhoneNo() {
		return billingAddressPhoneNo;
	}

	public void setBillingAddressPhoneNo(String billingAddressPhoneNo) {
		this.billingAddressPhoneNo = billingAddressPhoneNo;
	}

	public String getBillingAddressCountry() {
		return billingAddressCountry;
	}

	public void setBillingAddressCountry(String billingAddressCountry) {
		this.billingAddressCountry = billingAddressCountry;
	}

	public String getBillingAddressLine1() {
		return billingAddressLine1;
	}

	public void setBillingAddressLine1(String billingAddressLine1) {
		this.billingAddressLine1 = billingAddressLine1;
	}

	public String getBillingAddressState() {
		return billingAddressState;
	}

	public void setBillingAddressState(String billingAddressState) {
		this.billingAddressState = billingAddressState;
	}

	public String getBillingAddressCity() {
		return billingAddressCity;
	}

	public void setBillingAddressCity(String billingAddressCity) {
		this.billingAddressCity = billingAddressCity;
	}

	public String getBillingAddressPin() {
		return billingAddressPin;
	}

	public void setBillingAddressPin(String billingAddressPin) {
		this.billingAddressPin = billingAddressPin;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((billingAddressAttention == null) ? 0 : billingAddressAttention.hashCode());
		result = prime * result + ((billingAddressCity == null) ? 0 : billingAddressCity.hashCode());
		result = prime * result + ((billingAddressCountry == null) ? 0 : billingAddressCountry.hashCode());
		result = prime * result + ((billingAddressLine1 == null) ? 0 : billingAddressLine1.hashCode());
		result = prime * result + ((billingAddressPhoneNo == null) ? 0 : billingAddressPhoneNo.hashCode());
		result = prime * result + ((billingAddressPin == null) ? 0 : billingAddressPin.hashCode());
		result = prime * result + ((billingAddressState == null) ? 0 : billingAddressState.hashCode());
		result = prime * result + ((companyName == null) ? 0 : companyName.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((deliveryAddressAttention == null) ? 0 : deliveryAddressAttention.hashCode());
		result = prime * result + ((deliveryAddressCity == null) ? 0 : deliveryAddressCity.hashCode());
		result = prime * result + ((deliveryAddressCountry == null) ? 0 : deliveryAddressCountry.hashCode());
		result = prime * result + ((deliveryAddressLine1 == null) ? 0 : deliveryAddressLine1.hashCode());
		result = prime * result + ((deliveryAddressPhoneNo == null) ? 0 : deliveryAddressPhoneNo.hashCode());
		result = prime * result + ((deliveryAddressPin == null) ? 0 : deliveryAddressPin.hashCode());
		result = prime * result + ((deliveryAddressState == null) ? 0 : deliveryAddressState.hashCode());
		result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((mobile == null) ? 0 : mobile.hashCode());
		result = prime * result + ((primaryContact == null) ? 0 : primaryContact.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerUploadVo other = (CustomerUploadVo) obj;
		if (billingAddressAttention == null) {
			if (other.billingAddressAttention != null)
				return false;
		} else if (!billingAddressAttention.equals(other.billingAddressAttention))
			return false;
		if (billingAddressCity == null) {
			if (other.billingAddressCity != null)
				return false;
		} else if (!billingAddressCity.equals(other.billingAddressCity))
			return false;
		if (billingAddressCountry == null) {
			if (other.billingAddressCountry != null)
				return false;
		} else if (!billingAddressCountry.equals(other.billingAddressCountry))
			return false;
		if (billingAddressLine1 == null) {
			if (other.billingAddressLine1 != null)
				return false;
		} else if (!billingAddressLine1.equals(other.billingAddressLine1))
			return false;
		if (billingAddressPhoneNo == null) {
			if (other.billingAddressPhoneNo != null)
				return false;
		} else if (!billingAddressPhoneNo.equals(other.billingAddressPhoneNo))
			return false;
		if (billingAddressPin == null) {
			if (other.billingAddressPin != null)
				return false;
		} else if (!billingAddressPin.equals(other.billingAddressPin))
			return false;
		if (billingAddressState == null) {
			if (other.billingAddressState != null)
				return false;
		} else if (!billingAddressState.equals(other.billingAddressState))
			return false;
		if (companyName == null) {
			if (other.companyName != null)
				return false;
		} else if (!companyName.equals(other.companyName))
			return false;
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency))
			return false;
		if (deliveryAddressAttention == null) {
			if (other.deliveryAddressAttention != null)
				return false;
		} else if (!deliveryAddressAttention.equals(other.deliveryAddressAttention))
			return false;
		if (deliveryAddressCity == null) {
			if (other.deliveryAddressCity != null)
				return false;
		} else if (!deliveryAddressCity.equals(other.deliveryAddressCity))
			return false;
		if (deliveryAddressCountry == null) {
			if (other.deliveryAddressCountry != null)
				return false;
		} else if (!deliveryAddressCountry.equals(other.deliveryAddressCountry))
			return false;
		if (deliveryAddressLine1 == null) {
			if (other.deliveryAddressLine1 != null)
				return false;
		} else if (!deliveryAddressLine1.equals(other.deliveryAddressLine1))
			return false;
		if (deliveryAddressPhoneNo == null) {
			if (other.deliveryAddressPhoneNo != null)
				return false;
		} else if (!deliveryAddressPhoneNo.equals(other.deliveryAddressPhoneNo))
			return false;
		if (deliveryAddressPin == null) {
			if (other.deliveryAddressPin != null)
				return false;
		} else if (!deliveryAddressPin.equals(other.deliveryAddressPin))
			return false;
		if (deliveryAddressState == null) {
			if (other.deliveryAddressState != null)
				return false;
		} else if (!deliveryAddressState.equals(other.deliveryAddressState))
			return false;
		if (displayName == null) {
			if (other.displayName != null)
				return false;
		} else if (!displayName.equals(other.displayName))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (mobile == null) {
			if (other.mobile != null)
				return false;
		} else if (!mobile.equals(other.mobile))
			return false;
		if (primaryContact == null) {
			if (other.primaryContact != null)
				return false;
		} else if (!primaryContact.equals(other.primaryContact))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{primaryContact=");
		builder.append(primaryContact);
		builder.append(", companyName=");
		builder.append(companyName);
		builder.append(", displayName=");
		builder.append(displayName);
		builder.append(", email=");
		builder.append(email);
		builder.append(", mobile=");
		builder.append(mobile);
		builder.append(", currency=");
		builder.append(currency);
		builder.append(", deliveryAddressAttention=");
		builder.append(deliveryAddressAttention);
		builder.append(", deliveryAddressPhoneNo=");
		builder.append(deliveryAddressPhoneNo);
		builder.append(", deliveryAddressCountry=");
		builder.append(deliveryAddressCountry);
		builder.append(", deliveryAddressLine1=");
		builder.append(deliveryAddressLine1);
		builder.append(", deliveryAddressState=");
		builder.append(deliveryAddressState);
		builder.append(", deliveryAddressCity=");
		builder.append(deliveryAddressCity);
		builder.append(", deliveryAddressPin=");
		builder.append(deliveryAddressPin);
		builder.append(", billingAddressAttention=");
		builder.append(billingAddressAttention);
		builder.append(", billingAddressPhoneNo=");
		builder.append(billingAddressPhoneNo);
		builder.append(", billingAddressCountry=");
		builder.append(billingAddressCountry);
		builder.append(", billingAddressLine1=");
		builder.append(billingAddressLine1);
		builder.append(", billingAddressState=");
		builder.append(billingAddressState);
		builder.append(", billingAddressCity=");
		builder.append(billingAddressCity);
		builder.append(", billingAddressPin=");
		builder.append(billingAddressPin);
		builder.append("}");
		return builder.toString();
	}

}
