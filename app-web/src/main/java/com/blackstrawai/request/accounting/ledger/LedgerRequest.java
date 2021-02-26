package com.blackstrawai.request.accounting.ledger;

import java.util.List;

import com.blackstrawai.common.BaseRequest;
import com.blackstrawai.request.attachments.UploadFileRequest;

public class LedgerRequest extends BaseRequest{

	private Integer id;
	
	private Integer organizationId;
	
	private Boolean isSuperAdmin;
	
	private Integer accountGroupId;
	
	private Integer accountNameId;
	
	private String ledgerName;
	
	private String accountCode;
	
	private Double ledgerBalance;
	
	private String dateOfCreation;
	
	private Integer moduleId;
	
	private Boolean  ledgerStatus;
	
	private Boolean isSubledgerMandatory;
	
	private Integer entityId;
	
	private Boolean isBase;

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

	public Integer getAccountGroupId() {
		return accountGroupId;
	}

	public void setAccountGroupId(Integer accountGroupId) {
		this.accountGroupId = accountGroupId;
	}

	public Integer getAccountNameId() {
		return accountNameId;
	}

	public void setAccountNameId(Integer accountNameId) {
		this.accountNameId = accountNameId;
	}

	public String getLedgerName() {
		return ledgerName;
	}

	public void setLedgerName(String ledgerName) {
		this.ledgerName = ledgerName;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public Double getLedgerBalance() {
		return ledgerBalance;
	}

	public void setLedgerBalance(Double ledgerBalance) {
		this.ledgerBalance = ledgerBalance;
	}

	public String getDateOfCreation() {
		return dateOfCreation;
	}

	public void setDateOfCreation(String dateOfCreation) {
		this.dateOfCreation = dateOfCreation;
	}

	public Integer getModuleId() {
		return moduleId;
	}

	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}

	public Boolean getLedgerStatus() {
		return ledgerStatus;
	}

	public void setLedgerStatus(Boolean ledgerStatus) {
		this.ledgerStatus = ledgerStatus;
	}

	public Boolean getIsSubledgerMandatory() {
		return isSubledgerMandatory;
	}

	public void setIsSubledgerMandatory(Boolean isSubledgerMandatory) {
		this.isSubledgerMandatory = isSubledgerMandatory;
	}

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
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

	public Boolean getIsBase() {
		return isBase;
	}

	public void setIsBase(Boolean isBase) {
		this.isBase = isBase;
	}


	
	
}
