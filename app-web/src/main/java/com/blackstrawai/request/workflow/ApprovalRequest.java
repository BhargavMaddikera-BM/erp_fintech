package com.blackstrawai.request.workflow;

import com.blackstrawai.common.BaseRequest;

public class ApprovalRequest extends BaseRequest{
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
	public String getRejectionRemarks() {
		return rejectionRemarks;
	}
	public void setRejectionRemarks(String rejectionRemarks) {
		this.rejectionRemarks = rejectionRemarks;
	}
	public int getRejectionTypeId() {
		return rejectionTypeId;
	}
	public void setRejectionTypeId(int rejectionTypeId) {
		this.rejectionTypeId = rejectionTypeId;
	}
	
}