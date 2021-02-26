package com.blackstrawai.onboarding.organization;


import java.sql.Timestamp;
import java.util.List;

import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.TokenVo;

public class NewOrganizationVo  extends TokenVo{
	
	private Timestamp creatTs;
	private Timestamp updateTs;
	private int id;
	private String status;
	private OrganizationGeneralInfoVo generalInfo;
	private GSTDetailsVo gstDetails;
	private List<Integer> attachmentsToRemove;
	
	private KeyMembersVo keyMembers;	
	private List<UploadFileVo> attachments;
	private Boolean isSuperAdmin;
	private String rocData;
	
	public List<UploadFileVo> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<UploadFileVo> attachments) {
		this.attachments = attachments;
	}
	public Boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}
	public void setIsSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
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
	public OrganizationGeneralInfoVo getGeneralInfo() {
		return generalInfo;
	}
	public void setGeneralInfo(OrganizationGeneralInfoVo generalInfo) {
		this.generalInfo = generalInfo;
	}
	public GSTDetailsVo getGstDetails() {
		return gstDetails;
	}
	public void setGstDetails(GSTDetailsVo gstDetails) {
		this.gstDetails = gstDetails;
	}

	public KeyMembersVo getKeyMembers() {
		return keyMembers;
	}
	public void setKeyMembers(KeyMembersVo keyMembers) {
		this.keyMembers = keyMembers;
	}
	public Boolean isSuperAdmin() {
		return isSuperAdmin;
	}
	public void setSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}
	public String getRocData() {
		return rocData;
	}
	public void setRocData(String rocData) {
		this.rocData = rocData;
	}
	
	
	public List<Integer> getAttachmentsToRemove() {
		return attachmentsToRemove;
	}
	public void setAttachmentsToRemove(List<Integer> attachmentsToRemove) {
		this.attachmentsToRemove = attachmentsToRemove;
	}

}
