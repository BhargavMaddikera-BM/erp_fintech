package com.blackstrawai.request.accounting;

import java.util.List;

import com.blackstrawai.common.BaseRequest;
import com.blackstrawai.request.attachments.UploadFileRequest;

public class AccountingAspectsRequest extends BaseRequest {

	private AccountingAspectsGeneralRequest accountingAspectsGeneralInfo;
	private List<AccountingAspectsItemsRequest> itemDetails;
	private AccountingAspectsApproversRequest approversList;
	private List<UploadFileRequest> attachments;
	private List<Integer> attachmentsToRemove;
	private List<Integer> itemsToRemove;
	private Integer organizationId;
	private String status;
	private Boolean isSuperAdmin;
	private Integer id;
	private String roleName;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<Integer> getItemsToRemove() {
		return itemsToRemove;
	}

	public void setItemsToRemove(List<Integer> itemsToRemove) {
		this.itemsToRemove = itemsToRemove;
	}

	public AccountingAspectsApproversRequest getApproversList() {
		return approversList;
	}

	public void setApproversList(AccountingAspectsApproversRequest approversList) {
		this.approversList = approversList;
	}

	public AccountingAspectsGeneralRequest getAccountingAspectsGeneralInfo() {
		return accountingAspectsGeneralInfo;
	}

	public void setAccountingAspectsGeneralInfo(AccountingAspectsGeneralRequest accountingAspectsGeneralInfo) {
		this.accountingAspectsGeneralInfo = accountingAspectsGeneralInfo;
	}

	public List<AccountingAspectsItemsRequest> getItemDetails() {
		return itemDetails;
	}

	public void setItemDetails(List<AccountingAspectsItemsRequest> itemDetails) {
		this.itemDetails = itemDetails;
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

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}

	public void setIsSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
