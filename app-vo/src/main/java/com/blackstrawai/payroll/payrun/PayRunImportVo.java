package com.blackstrawai.payroll.payrun;




import com.blackstrawai.common.BaseVo;
import com.blackstrawai.upload.UploadFileVo;


public class PayRunImportVo extends BaseVo {
	
	private Integer payRunId;
	
	private Integer orgId;
	
	private String UserId;	
	
	private Boolean isSuperAdmin;
	
	private String roleName;
	
	private UploadFileVo payRunAttachment;

	public Integer getPayRunId() {
		return payRunId;
	}

	public void setPayRunId(Integer payRunId) {
		this.payRunId = payRunId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
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

	public UploadFileVo getPayRunAttachment() {
		return payRunAttachment;
	}

	public void setPayRunAttachment(UploadFileVo payRunAttachment) {
		this.payRunAttachment = payRunAttachment;
	}

	@Override
	public String toString() {
		return "PayRunImportVo [payRunId=" + payRunId + ", orgId=" + orgId + ", UserId=" + UserId + ", isSuperAdmin="
				+ isSuperAdmin + ", roleName=" + roleName + ", payRunAttachment=" + payRunAttachment + "]";
	}
	
	

}
