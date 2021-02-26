package com.blackstrawai.ap.dropdowns;

import com.blackstrawai.common.BaseVo;

public class PoDetailsDropDownVo extends BaseVo{
	private Integer id;
	private Integer vendorId;
	private String purchaseOrderNumber;
	private Double amount;
	private Integer currencyId;
	private String status;
	private Boolean isPaymentInitiated;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getVendorId() {
		return vendorId;
	}
	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}
	public String getPurchaseOrderNumber() {
		return purchaseOrderNumber;
	}
	public void setPurchaseOrderNumber(String purchaseOrderNumber) {
		this.purchaseOrderNumber = purchaseOrderNumber;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Integer getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Boolean getIsPaymentInitiated() {
		return isPaymentInitiated;
	}
	public void setIsPaymentInitiated(Boolean isPaymentInitiated) {
		this.isPaymentInitiated = isPaymentInitiated;
	}
	
	


}
