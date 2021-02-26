package com.blackstrawai.workflow;

import java.math.BigDecimal;

import com.blackstrawai.common.BaseVo;

public class WorkflowInvoiceVo extends BaseVo{
	
	private Integer id;		
	private Integer originalInvoiceId;		
	private Integer organizationId;	
	private String location;
	private int locationId;
	private Long vendorId;
	private boolean isRegistered;
	private boolean isGstEnabled;
	private String gstNumber;
	private String invoiceValue;
	private String vendorName;
	private int currentRuleId;
	private String pendingApprovalStatus;
	private String status;
	private String invoiceNumber;
	private boolean isInvoiceWithBills;
	private BigDecimal dueBalance;	
	private String currencySymbol;
	
	public Integer getOriginalInvoiceId() {
		return originalInvoiceId;
	}
	public void setOriginalInvoiceId(Integer originalInvoiceId) {
		this.originalInvoiceId = originalInvoiceId;
	}
	public boolean isInvoiceWithBills() {
		return isInvoiceWithBills;
	}
	public void setInvoiceWithBills(boolean isInvoiceWithBills) {
		this.isInvoiceWithBills = isInvoiceWithBills;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getLocationId() {
		return locationId;
	}
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}
	public Long getVendorId() {
		return vendorId;
	}
	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}
	public boolean isRegistered() {
		return isRegistered;
	}
	public void setRegistered(boolean isRegistered) {
		this.isRegistered = isRegistered;
	}
	public boolean isGstEnabled() {
		return isGstEnabled;
	}
	public void setGstEnabled(boolean isGstEnabled) {
		this.isGstEnabled = isGstEnabled;
	}
	public String getGstNumber() {
		return gstNumber;
	}
	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}
	public String getInvoiceValue() {
		return invoiceValue;
	}
	public void setInvoiceValue(String invoiceValue) {
		this.invoiceValue = invoiceValue;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public int getCurrentRuleId() {
		return currentRuleId;
	}
	public void setCurrentRuleId(int currentRuleId) {
		this.currentRuleId = currentRuleId;
	}
	public String getPendingApprovalStatus() {
		return pendingApprovalStatus;
	}
	public void setPendingApprovalStatus(String pendingApprovalStatus) {
		this.pendingApprovalStatus = pendingApprovalStatus;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	
	public BigDecimal getDueBalance() {
		return dueBalance;
	}
	public void setDueBalance(BigDecimal dueBalance) {
		this.dueBalance = dueBalance;
	}
	public String getCurrencySymbol() {
		return currencySymbol;
	}
	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}
	@Override
	public String toString() {
		return "WorkflowInvoiceVo [id=" + id + ", originalInvoiceId=" + originalInvoiceId + ", organizationId="
				+ organizationId + ", location=" + location + ", locationId=" + locationId + ", vendorId=" + vendorId
				+ ", isRegistered=" + isRegistered + ", isGstEnabled=" + isGstEnabled + ", gstNumber=" + gstNumber
				+ ", invoiceValue=" + invoiceValue + ", vendorName=" + vendorName + ", currentRuleId=" + currentRuleId
				+ ", pendingApprovalStatus=" + pendingApprovalStatus + ", status=" + status + ", invoiceNumber="
				+ invoiceNumber + ", isInvoiceWithBills=" + isInvoiceWithBills + "]";
	}
	
	

}
