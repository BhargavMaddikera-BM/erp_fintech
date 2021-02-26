package com.blackstrawai.request.keycontact.employee;

import java.sql.Timestamp;
import java.util.List;

import com.blackstrawai.common.BaseRequest;
import com.blackstrawai.keycontact.vendor.VendorBankDetailsVo;
import com.blackstrawai.request.attachments.UploadFileRequest;
import com.blackstrawai.request.keycontact.customer.BookKeepingSettingsRequest;

public class EmployeeRequest extends BaseRequest{
	
	private Integer id;
	private String status;
	private Timestamp updateTs;
	private Integer organizationId;
	private Boolean isSuperAdmin;
	private EmployeeGeneralInfoRequest employeeGeneralInfo;
	private List<VendorBankDetailsVo> bankDetails;
	private List<UploadFileRequest> attachments;
	private List<Integer> attachmentsToRemove;
	private String roleName;
	private List<Integer>itemsToRemove;
	private BookKeepingSettingsRequest bookKeepingSettings;
	

	public BookKeepingSettingsRequest getBookKeepingSettings() {
		return bookKeepingSettings;
	}
	public void setBookKeepingSettings(BookKeepingSettingsRequest bookKeepingSettings) {
		this.bookKeepingSettings = bookKeepingSettings;
	}
	public List<Integer> getItemsToRemove() {
		return itemsToRemove;
	}
	public void setItemsToRemove(List<Integer> itemsToRemove) {
		this.itemsToRemove = itemsToRemove;
	}
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
	public EmployeeGeneralInfoRequest getEmployeeGeneralInfo() {
		return employeeGeneralInfo;
	}
	public void setEmployeeGeneralInfo(EmployeeGeneralInfoRequest employeeGeneralInfo) {
		this.employeeGeneralInfo = employeeGeneralInfo;
	}
	public List<VendorBankDetailsVo> getBankDetails() {
		return bankDetails;
	}
	public void setBankDetails(List<VendorBankDetailsVo> bankDetails) {
		this.bankDetails = bankDetails;
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
	

}
