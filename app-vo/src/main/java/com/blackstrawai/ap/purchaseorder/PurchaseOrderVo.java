package com.blackstrawai.ap.purchaseorder;

import java.util.List;

import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseVo;

public class PurchaseOrderVo extends BaseVo{
	
	private Integer id;	
	
	private Integer organizationId;
	
	private Boolean isSuperAdmin;
	
	private PoGeneralInfoVo generalInformation;
	
	private PoAddressVo address;
	
	private PoItemsVo items;
	
	private List<UploadFileVo> attachments;
	
	private List<Integer> attachmentsToRemove ;
	
	private String status;
	
	private String roleName;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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

	public PoGeneralInfoVo getGeneralInformation() {
		return generalInformation;
	}

	public void setGeneralInformation(PoGeneralInfoVo generalInformation) {
		this.generalInformation = generalInformation;
	}

	public PoAddressVo getAddress() {
		return address;
	}

	public void setAddress(PoAddressVo address) {
		this.address = address;
	}

	public PoItemsVo getItems() {
		return items;
	}

	public void setItems(PoItemsVo items) {
		this.items = items;
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
	
}
