package com.blackstrawai.accounting;

import java.util.List;

import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseVo;

public class AccountingAspectsVo extends BaseVo {

	private AccountingAspectsGeneralVo accountingAspectsGeneralInfo;
	private List<AccountingAspectsItemsVo> itemDetails;
	private AccountingAspectsApproversVo approversList;
	private List<UploadFileVo> attachments;
	private List<Integer> attachmentsToRemove;
	private List<Integer> itemsToRemove;
	private Integer organizationId;
	private String status;
	private Boolean isSuperAdmin;
	private Integer id;
	private String roleName="Super Admin";

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

	public AccountingAspectsApproversVo getApproversList() {
		return approversList;
	}

	public void setApproversList(AccountingAspectsApproversVo approversList) {
		this.approversList = approversList;
	}

	public AccountingAspectsGeneralVo getAccountingAspectsGeneralInfo() {
		return accountingAspectsGeneralInfo;
	}

	public void setAccountingAspectsGeneralInfo(AccountingAspectsGeneralVo accountingAspectsGeneralInfo) {
		this.accountingAspectsGeneralInfo = accountingAspectsGeneralInfo;
	}

	public List<AccountingAspectsItemsVo> getItemDetails() {
		return itemDetails;
	}

	public void setItemDetails(List<AccountingAspectsItemsVo> itemDetails) {
		this.itemDetails = itemDetails;
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
