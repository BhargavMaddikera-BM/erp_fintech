package com.blackstrawai.workflow;

import com.blackstrawai.common.TokenVo;

public class ApprovalVo extends TokenVo {
	private int approvalId;
	private String organizationName;
	private int organizationId;
	private String status;
	private String roleName;
	private String rejectionRemarks;
	private int rejectionTypeId;

	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public int getApprovalId() {
		return approvalId;
	}
	public void setApprovalId(int approvalId) {
		this.approvalId = approvalId;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public int getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getRejectionTypeId() {
		return rejectionTypeId;
	}
	public void setRejectionTypeId(int rejectionTypeId) {
		this.rejectionTypeId = rejectionTypeId;
	}
	public String getRejectionRemarks() {
		return rejectionRemarks;
	}
	public void setRejectionRemarks(String rejectionRemarks) {
		this.rejectionRemarks = rejectionRemarks;
	}
	@Override
	public String toString() {
		return "ApprovalVo [approvalId=" + approvalId + ", organizationName=" + organizationName + ", organizationId="
				+ organizationId + ", status=" + status + ", roleName=" + roleName + ", rejectionRemarks="
				+ rejectionRemarks + ", rejectionTypeId=" + rejectionTypeId + "]";
	}

	
}