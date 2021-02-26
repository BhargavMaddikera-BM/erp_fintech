package com.blackstrawai.request.ar.template;

import java.util.List;

import com.blackstrawai.common.BaseRequest;
import com.blackstrawai.request.attachments.UploadFileRequest;

public class CreditNoteTemplateRequest extends BaseRequest {
	
private Integer templateId;
	
	private Integer orgId;
	
	private Boolean isSuperAdmin;
	
	private String UserId;
	
	private String roleName;
	
	private Long OrgContactNo;
	
	private CreditNoteTemplateInfoRequest templateInformation;	
		
	private List<UploadFileRequest> attachmentLogo;
	private List<UploadFileRequest> attachmentSign ;
	
	private List<Integer> attachmentsToRemove ;

	private String status;

	private String updateUserId;

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

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

	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Long getOrgContactNo() {
		return OrgContactNo;
	}

	public void setOrgContactNo(Long orgContactNo) {
		OrgContactNo = orgContactNo;
	}

	public CreditNoteTemplateInfoRequest getTemplateInformation() {
		return templateInformation;
	}

	public void setTemplateInformation(CreditNoteTemplateInfoRequest templateInformation) {
		this.templateInformation = templateInformation;
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

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	
	

}
