package com.blackstrawai.request.ap.template;

import java.util.List;


import com.blackstrawai.common.BaseRequest;
import com.blackstrawai.request.attachments.UploadFileRequest;

public class PurchaseOrderTemplateRequest extends BaseRequest {
	
   

	private Integer templateId;
	
	private Integer orgId;
	
	private Boolean isSuperAdmin;
	
	private String UserId;
	
	private String roleName;
	
	private Long OrgContactNo;
	
	private PurchaseOrderTemplateInfoRequest templateInformation;	
		
	private List<UploadFileRequest> attachmentLogo;
	private List<UploadFileRequest> attachmentSign ;
	
	private List<Integer> attachmentsToRemove ;

	private String status;

	private String updateUserId;
	
	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer id) {
		this.templateId = id;
	}

	public List<UploadFileRequest> getAttachmentLogo() {
		return attachmentLogo;
	}

	public void setAttachmentLogo(List<UploadFileRequest> attachmentLogo) {
		this.attachmentLogo = attachmentLogo;
	}

	public List<UploadFileRequest> getAttachmentSign() {
		return attachmentSign;
	}

	public void setAttachmentSign(List<UploadFileRequest> attachmentSign) {
		this.attachmentSign = attachmentSign;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getUpdateRoleName() {
		return updateRoleName;
	}

	public void setUpdateRoleName(String updateRoleName) {
		this.updateRoleName = updateRoleName;
	}

	private String updateRoleName;
	

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}

	public void setIsSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public PurchaseOrderTemplateInfoRequest getTemplateInformation() {
		return templateInformation;
	}

	public void setTemplateInformation(PurchaseOrderTemplateInfoRequest templateInformation) {
		this.templateInformation = templateInformation;
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

	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
	}
	
	public Long getOrgContactNo() {
		return OrgContactNo;
	}

	public void setOrgContactNo(Long orgContactNo) {
		OrgContactNo = orgContactNo;
	}

	@Override
	public String toString() {
		return "APPurchaseOrderTemplateRequest [templateId=" + templateId + ", orgId=" + orgId + ", isSuperAdmin="
				+ isSuperAdmin + ", UserId=" + UserId + ", roleName=" + roleName + ", OrgContactNo=" + OrgContactNo
				+ ", templateInformation=" + templateInformation + ", attachmentLogo=" + attachmentLogo
				+ ", attachmentSign=" + attachmentSign + ", attachmentsToRemove=" + attachmentsToRemove + ", status="
				+ status + ", updateUserId=" + updateUserId + ", updateRoleName=" + updateRoleName + "]";
	}
	
	

}
