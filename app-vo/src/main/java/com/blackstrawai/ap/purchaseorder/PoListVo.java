package com.blackstrawai.ap.purchaseorder;

public class PoListVo {

	private Integer poId;
	
	private String poNumber;
	
	private String vendorName;
	
	private String dateOfCreation;
	
	private String deliveryDate;
	
	private String status;
	
	private Double amount;
	
	private String currencySymbol;
	
	
	private Boolean isPaymentInitiated;
	
	
	
	public Boolean getIsPaymentInitiated() {
		return isPaymentInitiated;
	}

	public void setIsPaymentInitiated(Boolean isPaymentInitiated) {
		this.isPaymentInitiated = isPaymentInitiated;
	}
	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getDateOfCreation() {
		return dateOfCreation;
	}

	public void setDateOfCreation(String dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PoListVo [poId=");
		builder.append(poId);
		builder.append(", poNumber=");
		builder.append(poNumber);
		builder.append(", vendorName=");
		builder.append(vendorName);
		builder.append(", dateOfCreation=");
		builder.append(dateOfCreation);
		builder.append(", deliveryDate=");
		builder.append(deliveryDate);
		builder.append(", status=");
		builder.append(status);
		builder.append(", amount=");
		builder.append(amount);
		builder.append("]");
		return builder.toString();
	}
	
	
}
