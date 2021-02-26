package com.blackstrawai.ap.dropdowns;

import com.blackstrawai.common.BaseVo;

public class PaymentNonCoreBillDetailsDropDownVo extends BaseVo {

	private Integer id;
	private String vendorDisplayName;
	private String invoiceNo;
	private String poReferenceNo;
	private String invoiceDate;
	private String dueDate;
	private String status;
	private Double amount;
	private Double balanceDue;
	private String orgName;
	private Boolean isVendorEditable;
	private boolean isQuick;
	private String roleName;
	private String name;
	private int value;
	private Boolean isPaymentInitiated;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getVendorDisplayName() {
		return vendorDisplayName;
	}
	public void setVendorDisplayName(String vendorDisplayName) {
		this.vendorDisplayName = vendorDisplayName;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getPoReferenceNo() {
		return poReferenceNo;
	}
	public void setPoReferenceNo(String poReferenceNo) {
		this.poReferenceNo = poReferenceNo;
	}
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
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
	public Double getBalanceDue() {
		return balanceDue;
	}
	public void setBalanceDue(Double balanceDue) {
		this.balanceDue = balanceDue;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public Boolean getIsVendorEditable() {
		return isVendorEditable;
	}
	public void setIsVendorEditable(Boolean isVendorEditable) {
		this.isVendorEditable = isVendorEditable;
	}
	public boolean isQuick() {
		return isQuick;
	}
	public void setQuick(boolean isQuick) {
		this.isQuick = isQuick;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public Boolean getIsPaymentInitiated() {
		return isPaymentInitiated;
	}
	public void setIsPaymentInitiated(Boolean isPaymentInitiated) {
		this.isPaymentInitiated = isPaymentInitiated;
	}
	
	

}
