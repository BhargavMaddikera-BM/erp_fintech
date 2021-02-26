package com.blackstrawai.request.ar.invoice;

import java.util.List;

import com.blackstrawai.common.BaseRequest;
import com.blackstrawai.request.attachments.UploadFileRequest;

public class ArInvoiceRequest extends BaseRequest{

	private Integer invoiceId;
	
	private Integer orgId;
	
	private String roleName;
	
	private Boolean isSuperAdmin;
	
	private ArInvoiceGeneralInformationRequest generalInformation;
	
	private List<ArInvoiceProductRequest> products;

	private List<UploadFileRequest> attachments;
	
	private List<Integer> attachmentsToRemove ;
	
	private String status;
	
	public Integer getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}

	public void setIsSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public ArInvoiceGeneralInformationRequest getGeneralInformation() {
		return generalInformation;
	}

	public void setGeneralInformation(ArInvoiceGeneralInformationRequest generalInformation) {
		this.generalInformation = generalInformation;
	}

	public List<ArInvoiceProductRequest> getProducts() {
		return products;
	}

	public void setProducts(List<ArInvoiceProductRequest> products) {
		this.products = products;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
	
	
}
