package com.blackstrawai.ap.payment.noncore;

public class CreditDetailsVo {
	private Integer id;
	private String reference;
	private String originalAmt;
	private String availableAmt;
	private String adjustmentAmount;
	private String status;
	private boolean isDeleted;
	private boolean isCreatedNew;
	private int vendorId;
	private String name;
	private Integer value;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getOriginalAmt() {
		return originalAmt;
	}
	public void setOriginalAmt(String originalAmt) {
		this.originalAmt = originalAmt;
	}
	public String getAvailableAmt() {
		return availableAmt;
	}
	public void setAvailableAmt(String availableAmt) {
		this.availableAmt = availableAmt;
	}
	public String getAdjustmentAmount() {
		return adjustmentAmount;
	}
	public void setAdjustmentAmount(String adjustmentAmount) {
		this.adjustmentAmount = adjustmentAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public boolean isCreatedNew() {
		return isCreatedNew;
	}
	public void setCreatedNew(boolean isCreatedNew) {
		this.isCreatedNew = isCreatedNew;
	}
	public int getVendorId() {
		return vendorId;
	}
	public void setVendorId(int vendorId) {
		this.vendorId = vendorId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	
	
}
