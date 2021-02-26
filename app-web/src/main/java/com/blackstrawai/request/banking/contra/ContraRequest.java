package com.blackstrawai.request.banking.contra;

import java.sql.Date;
import java.util.List;

import com.blackstrawai.common.BaseRequest;
import com.blackstrawai.request.attachments.UploadFileRequest;
import com.fasterxml.jackson.annotation.JsonFormat;

public class ContraRequest extends BaseRequest {

	private String referenceNo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date date;
	private Integer baseCurrency;
	private String remark;
	private List<ContraEntriesRequest> contraEntries;
	private List<UploadFileRequest> attachments;
	private List<Integer> attachmentsToRemove;
	private String totalDebit;
	private String totalCredit;
	private String difference;
	private Boolean isSuperAdmin;
	private Integer orgId;
	private String status;
	private Integer id;
	private String roleName;
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	private List<Integer> itemsToRemove;

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

	public List<ContraEntriesRequest> getContraEntries() {
		return contraEntries;
	}

	public void setContraEntries(List<ContraEntriesRequest> contraEntries) {
		this.contraEntries = contraEntries;
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

	public Boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}

	public void setIsSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

}
