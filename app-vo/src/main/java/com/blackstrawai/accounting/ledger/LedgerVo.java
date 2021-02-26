package com.blackstrawai.accounting.ledger;

import java.util.List;

import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseVo;
import com.blackstrawai.settings.chartofaccounts.ChartOfAccountsLevel6Vo;

public class LedgerVo extends BaseVo{

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
	
	private List<ChartOfAccountsLevel6Vo> subledgers;
	
	private List<UploadFileVo> attachments;
	
	private String roleName;
	
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	private List<Integer> attachmentsToRemove ;

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
	
	public Boolean getIsBase() {
		return isBase;
	}

	public void setIsBase(Boolean isBase) {
		this.isBase = isBase;
	}

	public List<ChartOfAccountsLevel6Vo> getSubledgers() {
		return subledgers;
	}

	public void setSubledgers(List<ChartOfAccountsLevel6Vo> subledgers) {
		this.subledgers = subledgers;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LedgerVo [id=");
		builder.append(id);
		builder.append(", organizationId=");
		builder.append(organizationId);
		builder.append(", isSuperAdmin=");
		builder.append(isSuperAdmin);
		builder.append(", accountGroupId=");
		builder.append(accountGroupId);
		builder.append(", accountNameId=");
		builder.append(accountNameId);
		builder.append(", ledgerName=");
		builder.append(ledgerName);
		builder.append(", accountCode=");
		builder.append(accountCode);
		builder.append(", ledgerBalance=");
		builder.append(ledgerBalance);
		builder.append(", dateOfCreation=");
		builder.append(dateOfCreation);
		builder.append(", moduleId=");
		builder.append(moduleId);
		builder.append(", ledgerStatus=");
		builder.append(ledgerStatus);
		builder.append(", isSubledgerMandatory=");
		builder.append(isSubledgerMandatory);
		builder.append(", entityId=");
		builder.append(entityId);
		builder.append(", isBase=");
		builder.append(isBase);
		builder.append(", subledgers=");
		builder.append(subledgers);
		builder.append(", attachments=");
		builder.append(attachments);
		builder.append(", attachmentsToRemove=");
		builder.append(attachmentsToRemove);
		builder.append("]");
		return builder.toString();
	}

	
	
	
	
	
}
