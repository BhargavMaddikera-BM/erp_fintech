package com.blackstrawai.request.ap.billsinvoice;

import java.util.List;

import com.blackstrawai.common.BaseRequest;
import com.blackstrawai.request.attachments.UploadFileRequest;

public class QuickInvoiceRequest extends BaseRequest{

	private Integer id;
	
	private Integer organizationId;
	
	private Boolean isSuperAdmin;
	
	private Boolean isInvoiceWithBills;
	
	private Integer vendorId;
	
	private Integer currencyId;
	
	private String invoiceNumber;
	
	private String invoiceDate;
	
	private String dueDate;
	
	private Double baseAmount;
	
	private Double gstAmount;
	
	private Double totalAmount;
	
	private Boolean isRcmApplicable;
	
	private String remarks;
	
	private List<UploadFileRequest> attachments;
	
	private List<Integer> attachmentsToRemove ;
	
	private String roleName;

	private String aksharData;
	
	public String getAksharData() {
		return aksharData;
	}

	public void setAksharData(String aksharData) {
		this.aksharData = aksharData;
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

	public Boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}

	public void setIsSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public Boolean getIsInvoiceWithBills() {
		return isInvoiceWithBills;
	}

	public void setIsInvoiceWithBills(Boolean isInvoiceWithBills) {
		this.isInvoiceWithBills = isInvoiceWithBills;
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

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
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

	public Double getBaseAmount() {
		return baseAmount;
	}

	public void setBaseAmount(Double baseAmount) {
		this.baseAmount = baseAmount;
	}

	public Double getGstAmount() {
		return gstAmount;
	}

	public void setGstAmount(Double gstAmount) {
		this.gstAmount = gstAmount;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Boolean getIsRcmApplicable() {
		return isRcmApplicable;
	}

	public void setIsRcmApplicable(Boolean isRcmApplicable) {
		this.isRcmApplicable = isRcmApplicable;
	}

	public List<UploadFileRequest> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<UploadFileRequest> attachments) {
		this.attachments = attachments;
	}

	public List<Integer> getAttachmentsToRemove() {
		return attachmentsToRemove;
	}

	public void setAttachmentsToRemove(List<Integer> attachmentsToRemove) {
		this.attachmentsToRemove = attachmentsToRemove;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	
}
