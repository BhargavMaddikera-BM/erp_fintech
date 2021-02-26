package com.blackstrawai.request.ap.billsinvoice;

import java.util.List;

import com.blackstrawai.common.BaseRequest;
import com.blackstrawai.journals.GeneralLedgerVo;
import com.blackstrawai.request.attachments.UploadFileRequest;

public class InvoiceRequest extends BaseRequest{
	

	private Integer id;
	
	private String dateFormat;
	
	private Integer organizationId;
	
	private Boolean isSuperAdmin;
	
	private Boolean isInvoiceWithBills;
	
	private InvoiceGeneralInfoRequest generalInfo;
	
	private InvoiceTrasactionRequest transactionDetails;
	
	private List<UploadFileRequest> attachments;
	
	private List<Integer> attachmentsToRemove ;
	
	private GeneralLedgerVo generalLedgerData;
	
	private String status;
	
	private String roleName;

	

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

	public InvoiceGeneralInfoRequest getGeneralInfo() {
		return generalInfo;
	}

	public void setGeneralInfo(InvoiceGeneralInfoRequest generalInfo) {
		this.generalInfo = generalInfo;
	}

	public InvoiceTrasactionRequest getTransactionDetails() {
		return transactionDetails;
	}

	public void setTransactionDetails(InvoiceTrasactionRequest transactionDetails) {
		this.transactionDetails = transactionDetails;
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
	
	
	
	
	
}
