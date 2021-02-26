package com.blackstrawai.keycontact.employee;

import java.sql.Timestamp;
import java.util.List;

import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseVo;
import com.blackstrawai.common.BookKeepingSettingsVo;
import com.blackstrawai.keycontact.vendor.VendorBankDetailsVo;

public class EmployeeVo extends BaseVo {

	private String status;
	private Timestamp updateTs;
	private Integer organizationId;
	private Boolean isSuperAdmin;
	private EmployeeGeneralInfoVo employeeGeneralInfo;
	private List<VendorBankDetailsVo> bankDetails;
	private List<UploadFileVo> attachments;
	private List<Integer> attachmentsToRemove;
	private Integer id;
	private List<Integer>itemsToRemove;
	private BookKeepingSettingsVo bookKeepingSettings;

	public BookKeepingSettingsVo getBookKeepingSettings() {
		return bookKeepingSettings;
	}

	public void setBookKeepingSettings(BookKeepingSettingsVo bookKeepingSettings) {
		this.bookKeepingSettings = bookKeepingSettings;
	}

	public List<Integer> getItemsToRemove() {
		return itemsToRemove;
	}

	public void setItemsToRemove(List<Integer> itemsToRemove) {
		this.itemsToRemove = itemsToRemove;
	}

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getUpdateTs() {
		return updateTs;
	}

	public void setUpdateTs(Timestamp updateTs) {
		this.updateTs = updateTs;
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

	public EmployeeGeneralInfoVo getEmployeeGeneralInfo() {
		return employeeGeneralInfo;
	}

	public void setEmployeeGeneralInfo(EmployeeGeneralInfoVo employeeGeneralInfo) {
		this.employeeGeneralInfo = employeeGeneralInfo;
	}

	public List<VendorBankDetailsVo> getBankDetails() {
		return bankDetails;
	}

	public void setBankDetails(List<VendorBankDetailsVo> bankDetails) {
		this.bankDetails = bankDetails;
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
