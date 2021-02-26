package com.blackstrawai.ap.creditnote;

import java.util.List;

import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.journals.GeneralLedgerVo;

public class VendorCreditCreateVo {

	private Integer id;
	private Integer organizationId;
	private Integer userId;
	private String roleName;
	private boolean isSuperAdmin;
	private VendorCreditGeneralInformationVo generalInfo;
	private VendorCreditTransactionVo transactionDetails;
	private List<UploadFileVo> attachments;
	private List<Integer> attachmentsToRemove;
	private GeneralLedgerVo generalLedgerData;
	private String status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public boolean isSuperAdmin() {
		return isSuperAdmin;
	}

	public void setSuperAdmin(boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public VendorCreditGeneralInformationVo getGeneralInfo() {
		return generalInfo;
	}

	public void setGeneralInfo(VendorCreditGeneralInformationVo generalInfo) {
		this.generalInfo = generalInfo;
	}

	public VendorCreditTransactionVo getTransactionDetails() {
		return transactionDetails;
	}

	public void setTransactionDetails(VendorCreditTransactionVo transactionDetails) {
		this.transactionDetails = transactionDetails;
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

	public GeneralLedgerVo getGeneralLedgerData() {
		return generalLedgerData;
	}

	public void setGeneralLedgerData(GeneralLedgerVo generalLedgerData) {
		this.generalLedgerData = generalLedgerData;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "VendorCreditCreateRequest [id=" + id + ", organizationId=" + organizationId + ", userId=" + userId
				+ ", roleName=" + roleName + ", isSuperAdmin=" + isSuperAdmin + ", generalInfo=" + generalInfo
				+ ", transactionDetails=" + transactionDetails + ", attachments=" + attachments
				+ ", attachmentsToRemove=" + attachmentsToRemove + ", generalLedgerData=" + generalLedgerData
				+ ", status=" + status + "]";
	}

}
