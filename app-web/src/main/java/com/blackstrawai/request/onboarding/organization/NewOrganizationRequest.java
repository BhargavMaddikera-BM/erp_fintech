package com.blackstrawai.request.onboarding.organization;


import java.sql.Timestamp;
import java.util.List;

import com.blackstrawai.common.BaseRequest;
import com.blackstrawai.request.attachments.UploadFileRequest;

public class NewOrganizationRequest extends BaseRequest{
	
	private Timestamp creatTs;
	private Timestamp updateTs;
	private int id;
	private String status;
	private OrganizationGeneralInfoRequest generalInfo;
	private GSTDetailsRequest gstDetails;	
	private KeyMembersRequest keyMembers;
	private List<UploadFileRequest> attachments;
	
	private List<Integer> attachmentsToRemove;
	public List<UploadFileRequest> getAttachments() {
		return attachments;
	}
	public List<Integer> getAttachmentsToRemove() {
		return attachmentsToRemove;
	}
	public void setAttachmentsToRemove(List<Integer> attachmentsToRemove) {
		this.attachmentsToRemove = attachmentsToRemove;
	}
	public void setAttachments(List<UploadFileRequest> attachments) {
		this.attachments = attachments;
	}
	public Timestamp getCreatTs() {
		return creatTs;
	}
	public void setCreatTs(Timestamp creatTs) {
		this.creatTs = creatTs;
	}
	public Timestamp getUpdateTs() {
		return updateTs;
	}
	public void setUpdateTs(Timestamp updateTs) {
		this.updateTs = updateTs;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public OrganizationGeneralInfoRequest getGeneralInfo() {
		return generalInfo;
	}
	public void setGeneralInfo(OrganizationGeneralInfoRequest generalInfo) {
		this.generalInfo = generalInfo;
	}
	public GSTDetailsRequest getGstDetails() {
		return gstDetails;
	}
	public void setGstDetails(GSTDetailsRequest gstDetails) {
		this.gstDetails = gstDetails;
	}

	public KeyMembersRequest getKeyMembers() {
		return keyMembers;
	}
	public void setKeyMembers(KeyMembersRequest keyMembers) {
		this.keyMembers = keyMembers;
	}

	public Boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}
	public void setIsSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}
	public String getRocData() {
		return rocData;
	}
	public void setRocData(String rocData) {
		this.rocData = rocData;
	}
	private Boolean isSuperAdmin;
	private String rocData;
	
	

}
