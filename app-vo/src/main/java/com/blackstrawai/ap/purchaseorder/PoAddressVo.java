package com.blackstrawai.ap.purchaseorder;

public class PoAddressVo {
	
	private Boolean isSameBillingAddress;
	
	private PoBillingAddressVo billingAddress;
	
	private PoDeliveryAddressVo deliveryAddress;

	public PoBillingAddressVo getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(PoBillingAddressVo billingAddress) {
		this.billingAddress = billingAddress;
	}

	public PoDeliveryAddressVo getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(PoDeliveryAddressVo deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
	
	public Boolean getIsSameBillingAddress() {
		return isSameBillingAddress;
	}

	public void setIsSameBillingAddress(Boolean isSameBillingAddress) {
		this.isSameBillingAddress = isSameBillingAddress;
	}
}
