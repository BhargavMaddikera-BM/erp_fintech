package com.blackstrawai.banking.contra;

import java.sql.Date;
import java.util.List;

import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseVo;
import com.fasterxml.jackson.annotation.JsonFormat;

public class ContraVo extends BaseVo {

	private String referenceNo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date date;
	private Integer baseCurrency;
	private String remark;
	private String totalDebit;
	private String totalCredit;
	private String status;
	private Integer id;
	private List<ContraEntriesVo> contraEntries;
	private List<UploadFileVo> attachments;
	private List<Integer> attachmentsToRemove;
	private Boolean isSuperAdmin;
	private Integer orgId;
	private String difference;
	private List<Integer> itemsToRemove;
	private String roleName;
	private Date createTs;

	
	public Date getCreateTs() {
		return createTs;
	}

	public void setCreateTs(Date createTs) {
		this.createTs = createTs;
	}

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

	public String getDifference() {
		return difference;
	}

	public void setDifference(String difference) {
		this.difference = difference;
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

	public String getTotalDebit() {
		return totalDebit;
	}

	public void setTotalDebit(String totalDebit) {
		this.totalDebit = totalDebit;
	}

	public String getTotalCredit() {
		return totalCredit;
	}

	public void setTotalCredit(String totalCredit) {
		this.totalCredit = totalCredit;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(Integer baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<ContraEntriesVo> getContraEntries() {
		return contraEntries;
	}

	public void setContraEntries(List<ContraEntriesVo> contraEntries) {
		this.contraEntries = contraEntries;
	}

	public Boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}

	public void setIsSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

}
