package com.blackstrawai.request.ap.creditnote;

import com.blackstrawai.request.keycontact.customer.BaseAddressRequest;

public class VendorCreditAddress {

	private BaseAddressRequest vendorBillingAddress;
	private BaseAddressRequest vendorDeliveryAddress;
	private BaseAddressRequest organizationBillingAddress;
	private BaseAddressRequest organizationDeliveryAddress;
	private Boolean isSameBillingAddressForOrganization;

	public BaseAddressRequest getVendorBillingAddress() {
		return vendorBillingAddress;
	}

	public void setVendorBillingAddress(BaseAddressRequest vendorBillingAddress) {
		this.vendorBillingAddress = vendorBillingAddress;
	}

	public BaseAddressRequest getVendorDeliveryAddress() {
		return vendorDeliveryAddress;
	}

	public void setVendorDeliveryAddress(BaseAddressRequest vendorDeliveryAddress) {
		this.vendorDeliveryAddress = vendorDeliveryAddress;
	}

	public BaseAddressRequest getOrganizationBillingAddress() {
		return organizationBillingAddress;
	}

	public void setOrganizationBillingAddress(BaseAddressRequest organizationBillingAddress) {
		this.organizationBillingAddress = organizationBillingAddress;
	}

	public BaseAddressRequest getOrganizationDeliveryAddress() {
		return organizationDeliveryAddress;
	}

	public void setOrganizationDeliveryAddress(BaseAddressRequest organizationDeliveryAddress) {
		this.organizationDeliveryAddress = organizationDeliveryAddress;
	}

	public Boolean getIsSameBillingAddressForOrganization() {
		return isSameBillingAddressForOrganization;
	}

	public void setIsSameBillingAddressForOrganization(Boolean isSameBillingAddressForOrganization) {
		this.isSameBillingAddressForOrganization = isSameBillingAddressForOrganization;
	}

	@Override
	public String toString() {
		return "VendorCreditAddress [vendorBillingAddress=" + vendorBillingAddress + ", vendorDeliveryAddress="
				+ vendorDeliveryAddress + ", organizationBillingAddress=" + organizationBillingAddress
				+ ", organizationDeliveryAddress=" + organizationDeliveryAddress
				+ ", isSameBillingAddressForOrganization=" + isSameBillingAddressForOrganization + "]";
	}

}
