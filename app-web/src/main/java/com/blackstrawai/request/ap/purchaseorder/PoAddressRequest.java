package com.blackstrawai.request.ap.purchaseorder;

public class PoAddressRequest {
	
	private Boolean isSameBillingAddress;
	
	private PoBillingAddressRequest billingAddress;
	
	private PoDeliveryAddressRequest deliveryAddress;

	public PoBillingAddressRequest getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(PoBillingAddressRequest billingAddress) {
		this.billingAddress = billingAddress;
	}

	public PoDeliveryAddressRequest getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(PoDeliveryAddressRequest deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
	
	public Boolean getIsSameBillingAddress() {
		return isSameBillingAddress;
	}

	public void setIsSameBillingAddress(Boolean isSameBillingAddress) {
		this.isSameBillingAddress = isSameBillingAddress;
	}
	
}
