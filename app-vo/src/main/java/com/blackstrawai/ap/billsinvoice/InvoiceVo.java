package com.blackstrawai.ap.billsinvoice;

import java.util.List;

import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseVo;
import com.blackstrawai.journals.GeneralLedgerVo;

public class InvoiceVo extends BaseVo{
	
	private Integer id;
	
	private String dateFormat;
	
	private Integer organizationId;
	
	private Boolean isSuperAdmin;
	
	private Boolean isInvoiceWithBills;
	
	private Boolean isVendorEditable;
	
	private InvoiceGeneralInfoVo generalInfo;
	
	private InvoiceTrasactionVo transactionDetails;
	
	private List<UploadFileVo> attachments;
	
	private List<Integer> attachmentsToRemove ;
	
	private String status;
	
	private String roleName;
	
	private String createdBy;
	
	private GeneralLedgerVo generalLedgerData;


	public GeneralLedgerVo getGeneralLedgerData() {
		return generalLedgerData;
	}

	public void setGeneralLedgerData(GeneralLedgerVo generalLedgerData) {
		this.generalLedgerData = generalLedgerData;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
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

	public InvoiceGeneralInfoVo getGeneralInfo() {
		return generalInfo;
	}

	public void setGeneralInfo(InvoiceGeneralInfoVo generalInfo) {
		this.generalInfo = generalInfo;
	}

	public InvoiceTrasactionVo getTransactionDetails() {
		return transactionDetails;
	}

	public void setTransactionDetails(InvoiceTrasactionVo transactionDetails) {
		this.transactionDetails = transactionDetails;
	}

	public Boolean getIsVendorEditable() {
		return isVendorEditable;
	}

	public void setIsVendorEditable(Boolean isVendorEditable) {
		this.isVendorEditable = isVendorEditable;
	}

	public List<UploadFileVo> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<UploadFileVo> attachments) {
		this.attachments = attachments;
	}

	public List<Integer> getAttachmentsToRemove() {
		return attachmentsToRemove;
	}

	public void setAttachmentsToRemove(List<Integer> attachmentsToRemove) {
		this.attachmentsToRemove = attachmentsToRemove;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getIsInvoiceWithBills() {
		return isInvoiceWithBills;
	}

	public void setIsInvoiceWithBills(Boolean isInvoiceWithBills) {
		this.isInvoiceWithBills = isInvoiceWithBills;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InvoiceVo [id=");
		builder.append(id);
		builder.append(", dateFormat=");
		builder.append(dateFormat);
		builder.append(", organizationId=");
		builder.append(organizationId);
		builder.append(", isSuperAdmin=");
		builder.append(isSuperAdmin);
		builder.append(", isInvoiceWithBills=");
		builder.append(isInvoiceWithBills);
		builder.append(", generalInfo=");
		builder.append(generalInfo);
		builder.append(", transactionDetails=");
		builder.append(transactionDetails);
		builder.append(", attachments=");
		builder.append(attachments);
		builder.append(", attachmentsToRemove=");
		builder.append(attachmentsToRemove);
		builder.append(", status=");
		builder.append(status);
		builder.append(", roleName=");
		builder.append(roleName);
		builder.append("]");
		return builder.toString();
	}

	
	
}
