package com.blackstrawai.ap.creditnote;

import com.blackstrawai.common.BaseAddressVo;

public class VendorCreditAddressVo {

	private BaseAddressVo vendorBillingAddress;
	private BaseAddressVo vendorDeliveryAddress;
	private BaseAddressVo organizationBillingAddress;
	private BaseAddressVo organizationDeliveryAddress;
	private Boolean isSameBillingAddressForOrganization;

	public BaseAddressVo getVendorBillingAddress() {
		return vendorBillingAddress;
	}

	public void setVendorBillingAddress(BaseAddressVo vendorBillingAddress) {
		this.vendorBillingAddress = vendorBillingAddress;
	}

	public BaseAddressVo getVendorDeliveryAddress() {
		return vendorDeliveryAddress;
	}

	public void setVendorDeliveryAddress(BaseAddressVo vendorDeliveryAddress) {
		this.vendorDeliveryAddress = vendorDeliveryAddress;
	}

	public BaseAddressVo getOrganizationBillingAddress() {
		return organizationBillingAddress;
	}

	public void setOrganizationBillingAddress(BaseAddressVo organizationBillingAddress) {
		this.organizationBillingAddress = organizationBillingAddress;
	}

	public BaseAddressVo getOrganizationDeliveryAddress() {
		return organizationDeliveryAddress;
	}

	public void setOrganizationDeliveryAddress(BaseAddressVo organizationDeliveryAddress) {
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
		return "VendorCreditAddressVo [vendorBillingAddress=" + vendorBillingAddress + ", vendorDeliveryAddress="
				+ vendorDeliveryAddress + ", organizationBillingAddress=" + organizationBillingAddress
				+ ", organizationDeliveryAddress=" + organizationDeliveryAddress
				+ ", isSameBillingAddressForOrganization=" + isSameBillingAddressForOrganization + "]";
	}

}
