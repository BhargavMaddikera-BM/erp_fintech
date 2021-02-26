package com.blackstrawai.request.payroll.PayRun;



import com.blackstrawai.common.BaseRequest;
import com.blackstrawai.request.attachments.UploadFileRequest;

public class PayRunImportRequest extends BaseRequest {

	    private Integer payRunId;
		
		private Integer orgId;
		
		private String UserId;	
		
		private Boolean isSuperAdmin;
		
		private String roleName;
		
		private UploadFileRequest payRunAttachment;

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

		public UploadFileRequest getPayRunAttachment() {
			return payRunAttachment;
		}

		public void setPayRunAttachment(UploadFileRequest payRunAttachment) {
			this.payRunAttachment = payRunAttachment;
		}
		
		
}
