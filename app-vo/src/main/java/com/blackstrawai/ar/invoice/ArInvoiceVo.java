package com.blackstrawai.ar.invoice;

import java.util.List;

import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseVo;

public class ArInvoiceVo extends BaseVo{

	private Integer invoiceId;
	
	private Integer orgId;
	
	private String roleName;
	
	private Boolean isSuperAdmin;
	
	private ArInvoiceGeneralInformationVo generalInformation;
	
	private List<ArInvoiceProductVo> products;
	
	private List<ArInvoiceTaxDistributionVo> groupedTax;
	
	private List<UploadFileVo> attachments;
	
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

	public ArInvoiceGeneralInformationVo getGeneralInformation() {
		return generalInformation;
	}

	public void setGeneralInformation(ArInvoiceGeneralInformationVo generalInformation) {
		this.generalInformation = generalInformation;
	}

	public List<ArInvoiceProductVo> getProducts() {
		return products;
	}

	public void setProducts(List<ArInvoiceProductVo> products) {
		this.products = products;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<ArInvoiceTaxDistributionVo> getGroupedTax() {
		return groupedTax;
	}

	public void setGroupedTax(List<ArInvoiceTaxDistributionVo> groupedTax) {
		this.groupedTax = groupedTax;
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
	
	
}
