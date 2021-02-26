package com.blackstrawai.ap.template;

import java.util.List;

import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseVo;

public class PurchaseOrderTemplateVo extends BaseVo {
	
    private Integer templateId;
	
	private Integer orgId;
	
	private String UserId;	
	
	private Boolean isSuperAdmin;
	
	private String roleName;
	
	private Long OrgContactNo;
	
	private PurchaseOrderTemplateInformationVo templateInformation;	
		
	private List<UploadFileVo> attachmentLogo;
	private List<UploadFileVo> attachmentSign ;
	
	private List<Integer> attachmentsToRemove ;

	private String status;

	
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

	public PurchaseOrderTemplateInformationVo getTemplateInformation() {
		return templateInformation;
	}

	public void setTemplateInformation(PurchaseOrderTemplateInformationVo templateInformation) {
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

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer id) {
		this.templateId = id;
	}

	public List<UploadFileVo> getAttachmentLogo() {
		return attachmentLogo;
	}

	public void setAttachmentLogo(List<UploadFileVo> attachmentLogo) {
		this.attachmentLogo = attachmentLogo;
	}

	public List<UploadFileVo> getAttachmentSign() {
		return attachmentSign;
	}

	public void setAttachmentSign(List<UploadFileVo> attachmentSign) {
		this.attachmentSign = attachmentSign;
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
		return "APPurchaseOrderTemplateVo [templateId=" + templateId + ", orgId=" + orgId + ", UserId=" + UserId
				+ ", isSuperAdmin=" + isSuperAdmin + ", roleName=" + roleName + ", OrgContactNo=" + OrgContactNo
				+ ", templateInformation=" + templateInformation + ", attachmentLogo=" + attachmentLogo
				+ ", attachmentSign=" + attachmentSign + ", attachmentsToRemove=" + attachmentsToRemove + ", status="
				+ status + "]";
	}

	

}
