package com.blackstrawai.request.ap.purchaseorder;

import java.util.List;

import com.blackstrawai.common.BaseRequest;
import com.blackstrawai.request.attachments.UploadFileRequest;

public class PurchaseOrderRequest extends BaseRequest{

	private Integer id;	
	
	private Integer organizationId;
	
	private Boolean isSuperAdmin;

	private PoGeneralInfoRequest generalInformation;
	
	private PoAddressRequest address;
	
	private PoItemsRequest items;
	
	private String status;
	
	private List<UploadFileRequest> attachments;
	
	private List<Integer> attachmentsToRemove ;
	
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

	public PoGeneralInfoRequest getGeneralInformation() {
		return generalInformation;
	}

	public void setGeneralInformation(PoGeneralInfoRequest generalInformation) {
		this.generalInformation = generalInformation;
	}

	public PoAddressRequest getAddress() {
		return address;
	}

	public void setAddress(PoAddressRequest address) {
		this.address = address;
	}

	public PoItemsRequest getItems() {
		return items;
	}

	public void setItems(PoItemsRequest items) {
		this.items = items;
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


	
	
	
}
