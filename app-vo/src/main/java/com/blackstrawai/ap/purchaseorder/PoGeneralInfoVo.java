package com.blackstrawai.ap.purchaseorder;

public class PoGeneralInfoVo {

private Integer vendorId;
	
	private String vendorGstNumber;
	
	private String purchaseOrderDate;
	
	private String poOrderNoSuffix;
	
	private String purchaseOrderNo;
	
	private String poOrderNoPrefix;
	
	private Integer locationId;
	
	private String locationGstNumber;
	
	private String deliveryDate;
	
	private String referenceNo;
	
	private Integer shippingPreferenceId;
	
	private Integer shippingMethodId;
	
	private Integer paymentTermsId;
	
	private String notes;
	
	private String termsConditions;
	
	private Integer purchaseOrderTypeId;

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorGstNumber() {
		return vendorGstNumber;
	}

	public void setVendorGstNumber(String vendorGstNumber) {
		this.vendorGstNumber = vendorGstNumber;
	}

	public String getPurchaseOrderDate() {
		return purchaseOrderDate;
	}

	public void setPurchaseOrderDate(String purchaseOrderDate) {
		this.purchaseOrderDate = purchaseOrderDate;
	}

	public String getPoOrderNoSuffix() {
		return poOrderNoSuffix;
	}

	public void setPoOrderNoSuffix(String poOrderNoSuffix) {
		this.poOrderNoSuffix = poOrderNoSuffix;
	}

	public String getPurchaseOrderNo() {
		return purchaseOrderNo;
	}

	public void setPurchaseOrderNo(String purchaseOrderNo) {
		this.purchaseOrderNo = purchaseOrderNo;
	}

	public String getPoOrderNoPrefix() {
		return poOrderNoPrefix;
	}

	public void setPoOrderNoPrefix(String poOrderNoPrefix) {
		this.poOrderNoPrefix = poOrderNoPrefix;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getLocationGstNumber() {
		return locationGstNumber;
	}

	public void setLocationGstNumber(String locationGstNumber) {
		this.locationGstNumber = locationGstNumber;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public Integer getShippingPreferenceId() {
		return shippingPreferenceId;
	}

	public void setShippingPreferenceId(Integer shippingPreferenceId) {
		this.shippingPreferenceId = shippingPreferenceId;
	}

	public Integer getShippingMethodId() {
		return shippingMethodId;
	}

	public void setShippingMethodId(Integer shippingMethodId) {
		this.shippingMethodId = shippingMethodId;
	}

	public Integer getPaymentTermsId() {
		return paymentTermsId;
	}

	public void setPaymentTermsId(Integer paymentTermsId) {
		this.paymentTermsId = paymentTermsId;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getTermsConditions() {
		return termsConditions;
	}

	public void setTermsConditions(String termsConditions) {
		this.termsConditions = termsConditions;
	}

	public Integer getPurchaseOrderTypeId() {
		return purchaseOrderTypeId;
	}

	public void setPurchaseOrderTypeId(Integer purchaseOrderTypeId) {
		this.purchaseOrderTypeId = purchaseOrderTypeId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PoGeneralInfoVo [vendorId=");
		builder.append(vendorId);
		builder.append(", vendorGstNumber=");
		builder.append(vendorGstNumber);
		builder.append(", purchaseOrderDate=");
		builder.append(purchaseOrderDate);
		builder.append(", poOrderNoSuffix=");
		builder.append(poOrderNoSuffix);
		builder.append(", purchaseOrderNo=");
		builder.append(purchaseOrderNo);
		builder.append(", poOrderNoPrefix=");
		builder.append(poOrderNoPrefix);
		builder.append(", locationId=");
		builder.append(locationId);
		builder.append(", locationGstNumber=");
		builder.append(locationGstNumber);
		builder.append(", deliveryDate=");
		builder.append(deliveryDate);
		builder.append(", referenceNo=");
		builder.append(referenceNo);
		builder.append(", shippingPreferenceId=");
		builder.append(shippingPreferenceId);
		builder.append(", shippingMethodId=");
		builder.append(shippingMethodId);
		builder.append(", paymentTermsId=");
		builder.append(paymentTermsId);
		builder.append(", notes=");
		builder.append(notes);
		builder.append(", termsConditions=");
		builder.append(termsConditions);
		builder.append(", purchaseOrderTypeId=");
		builder.append(purchaseOrderTypeId);
		builder.append("]");
		return builder.toString();
	}
	
	
}
