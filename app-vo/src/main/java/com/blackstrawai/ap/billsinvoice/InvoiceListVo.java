package com.blackstrawai.ap.billsinvoice;

import java.util.List;

import com.blackstrawai.common.BaseVo;

public class InvoiceListVo extends BaseVo implements Comparable<InvoiceListVo>{
	private Integer invoiceId;
	
	private String vendorDisplayName;
	
	private String invoiceNo;
	
	private String poReferenceNo;
	
	private String invoiceDate;
	
	private String dueDate;
	
	private String status;
	
	private String pendingApprovalStatus;
	
	private Double amount; 
	
	private Double balanceDue;
	
	private String orgName;

	private Boolean isVendorEditable;
	
	private boolean isQuick;
	
	private String roleName;
	
	private String name;
	
	private Integer value;
	
	private Integer vendorId;
	
	private Integer currencyId;
	
	private boolean isVoidable;
	
	private List<CommonTransactionVo> trasactions;
	
	private String currencySymbol;
	
	private Boolean isPaymentInitiated;
	
	
	
	public Boolean getIsPaymentInitiated() {
		return isPaymentInitiated;
	}

	public void setIsPaymentInitiated(Boolean isPaymentInitiated) {
		this.isPaymentInitiated = isPaymentInitiated;
	}

	public String getPendingApprovalStatus() {
		return pendingApprovalStatus;
	}

	public void setPendingApprovalStatus(String pendingApprovalStatus) {
		this.pendingApprovalStatus = pendingApprovalStatus;
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

	public Integer getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
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

	public Double getBalanceDue() {
		return balanceDue;
	}

	public void setBalanceDue(Double balanceDue) {
		this.balanceDue = balanceDue;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
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

	public boolean getIsQuick() {
		return isQuick;
	}

	public void setIsQuick(boolean isQuick) {
		this.isQuick = isQuick;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public boolean getIsVoidable() {
		return isVoidable;
	}

	public void setIsVoidable(boolean isVoidable) {
		this.isVoidable = isVoidable;
	}

	public List<CommonTransactionVo> getTrasactions() {
		return trasactions;
	}

	public void setTrasactions(List<CommonTransactionVo> trasactions) {
		this.trasactions = trasactions;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	@Override
	public int compareTo(InvoiceListVo invoice) {
		if(invoice!=null && invoice.getStatus().equalsIgnoreCase("OVERDUE")){
			return 1;
		}else {
			return -1;
		}
	}
	
	
	
}
