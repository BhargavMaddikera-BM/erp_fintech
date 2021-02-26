package com.blackstrawai.request.ap.balanceconfirmation;

import java.sql.Date;
import java.util.List;

import com.blackstrawai.common.BaseRequest;
import com.blackstrawai.request.attachments.UploadFileRequest;
import com.fasterxml.jackson.annotation.JsonFormat;

public class BalanceConfirmationRequest extends BaseRequest {
	private Integer id;
	private Integer vendorId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date startDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date endDate;
	private Integer organizationId;
	private Boolean isSuperAdmin;
	private String roleName;
	private List<BalanceConfirmationGeneralRequest> generalInfo;
	private List<UploadFileRequest> attachments;
	private List<Integer> attachmentsToRemove;
	private String status;
	private Boolean isQuick;

	public Boolean getIsQuick() {
		return isQuick;
	}

	public void setIsQuick(Boolean isQuick) {
		this.isQuick = isQuick;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

	public List<BalanceConfirmationGeneralRequest> getGeneralInfo() {
		return generalInfo;
	}

	public void setGeneralInfo(List<BalanceConfirmationGeneralRequest> generalInfo) {
		this.generalInfo = generalInfo;
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
