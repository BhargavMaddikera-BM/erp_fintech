package com.blackstrawai.upload;

public class VendorUploadVo {

	private String primaryContact;
	private String companyName;
	private String vendorDisplayName;
	private String email;
	private String paymentTerms;
	private String currency;
	private String tds;
	private String originAddressAttention;
	private String originAddressPhone;
	private String originAddressCountry;
	private String originAddressLine1;
	private String originAddressState;
	private String originAddressCity;
	private String originAddressPin;
	private String billingAddressAttention;
	private String billingAddressPhone;
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

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getTds() {
		return tds;
	}

	public void setTds(String tds) {
		this.tds = tds;
	}

	public String getOriginAddressAttention() {
		return originAddressAttention;
	}

	public void setOriginAddressAttention(String originAddressAttention) {
		this.originAddressAttention = originAddressAttention;
	}

	public String getOriginAddressPhone() {
		return originAddressPhone;
	}

	public void setOriginAddressPhone(String originAddressPhone) {
		this.originAddressPhone = originAddressPhone;
	}

	public String getOriginAddressCountry() {
		return originAddressCountry;
	}

	public void setOriginAddressCountry(String originAddressCountry) {
		this.originAddressCountry = originAddressCountry;
	}

	public String getOriginAddressLine1() {
		return originAddressLine1;
	}

	public void setOriginAddressLine1(String originAddressLine1) {
		this.originAddressLine1 = originAddressLine1;
	}

	public String getOriginAddressState() {
		return originAddressState;
	}

	public void setOriginAddressState(String originAddressState) {
		this.originAddressState = originAddressState;
	}

	public String getOriginAddressCity() {
		return originAddressCity;
	}

	public void setOriginAddressCity(String originAddressCity) {
		this.originAddressCity = originAddressCity;
	}

	public String getOriginAddressPin() {
		return originAddressPin;
	}

	public void setOriginAddressPin(String originAddressPin) {
		this.originAddressPin = originAddressPin;
	}

	public String getBillingAddressAttention() {
		return billingAddressAttention;
	}

	public void setBillingAddressAttention(String billingAddressAttention) {
		this.billingAddressAttention = billingAddressAttention;
	}

	public String getBillingAddressPhone() {
		return billingAddressPhone;
	}

	public void setBillingAddressPhone(String billingAddressPhone) {
		this.billingAddressPhone = billingAddressPhone;
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
		result = prime * result + ((billingAddressPhone == null) ? 0 : billingAddressPhone.hashCode());
		result = prime * result + ((billingAddressPin == null) ? 0 : billingAddressPin.hashCode());
		result = prime * result + ((billingAddressState == null) ? 0 : billingAddressState.hashCode());
		result = prime * result + ((companyName == null) ? 0 : companyName.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((originAddressAttention == null) ? 0 : originAddressAttention.hashCode());
		result = prime * result + ((originAddressCity == null) ? 0 : originAddressCity.hashCode());
		result = prime * result + ((originAddressCountry == null) ? 0 : originAddressCountry.hashCode());
		result = prime * result + ((originAddressLine1 == null) ? 0 : originAddressLine1.hashCode());
		result = prime * result + ((originAddressPhone == null) ? 0 : originAddressPhone.hashCode());
		result = prime * result + ((originAddressPin == null) ? 0 : originAddressPin.hashCode());
		result = prime * result + ((originAddressState == null) ? 0 : originAddressState.hashCode());
		result = prime * result + ((paymentTerms == null) ? 0 : paymentTerms.hashCode());
		result = prime * result + ((primaryContact == null) ? 0 : primaryContact.hashCode());
		result = prime * result + ((tds == null) ? 0 : tds.hashCode());
		result = prime * result + ((vendorDisplayName == null) ? 0 : vendorDisplayName.hashCode());
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
		VendorUploadVo other = (VendorUploadVo) obj;
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
		if (billingAddressPhone == null) {
			if (other.billingAddressPhone != null)
				return false;
		} else if (!billingAddressPhone.equals(other.billingAddressPhone))
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
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (originAddressAttention == null) {
			if (other.originAddressAttention != null)
				return false;
		} else if (!originAddressAttention.equals(other.originAddressAttention))
			return false;
		if (originAddressCity == null) {
			if (other.originAddressCity != null)
				return false;
		} else if (!originAddressCity.equals(other.originAddressCity))
			return false;
		if (originAddressCountry == null) {
			if (other.originAddressCountry != null)
				return false;
		} else if (!originAddressCountry.equals(other.originAddressCountry))
			return false;
		if (originAddressLine1 == null) {
			if (other.originAddressLine1 != null)
				return false;
		} else if (!originAddressLine1.equals(other.originAddressLine1))
			return false;
		if (originAddressPhone == null) {
			if (other.originAddressPhone != null)
				return false;
		} else if (!originAddressPhone.equals(other.originAddressPhone))
			return false;
		if (originAddressPin == null) {
			if (other.originAddressPin != null)
				return false;
		} else if (!originAddressPin.equals(other.originAddressPin))
			return false;
		if (originAddressState == null) {
			if (other.originAddressState != null)
				return false;
		} else if (!originAddressState.equals(other.originAddressState))
			return false;
		if (paymentTerms == null) {
			if (other.paymentTerms != null)
				return false;
		} else if (!paymentTerms.equals(other.paymentTerms))
			return false;
		if (primaryContact == null) {
			if (other.primaryContact != null)
				return false;
		} else if (!primaryContact.equals(other.primaryContact))
			return false;
		if (tds == null) {
			if (other.tds != null)
				return false;
		} else if (!tds.equals(other.tds))
			return false;
		if (vendorDisplayName == null) {
			if (other.vendorDisplayName != null)
				return false;
		} else if (!vendorDisplayName.equals(other.vendorDisplayName))
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
		builder.append(", vendorDisplayName=");
		builder.append(vendorDisplayName);
		builder.append(", email=");
		builder.append(email);
		builder.append(", paymentTerms=");
		builder.append(paymentTerms);
		builder.append(", currency=");
		builder.append(currency);
		builder.append(", tds=");
		builder.append(tds);
		builder.append(", originAddressAttention=");
		builder.append(originAddressAttention);
		builder.append(", originAddressPhone=");
		builder.append(originAddressPhone);
		builder.append(", originAddressCountry=");
		builder.append(originAddressCountry);
		builder.append(", originAddressLine1=");
		builder.append(originAddressLine1);
		builder.append(", originAddressState=");
		builder.append(originAddressState);
		builder.append(", originAddressCity=");
		builder.append(originAddressCity);
		builder.append(", originAddressPin=");
		builder.append(originAddressPin);
		builder.append(", billingAddressAttention=");
		builder.append(billingAddressAttention);
		builder.append(", billingAddressPhone=");
		builder.append(billingAddressPhone);
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
