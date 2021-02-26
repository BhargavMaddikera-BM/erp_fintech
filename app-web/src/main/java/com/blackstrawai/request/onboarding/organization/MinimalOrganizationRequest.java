package com.blackstrawai.request.onboarding.organization;

import java.util.List;
import java.util.Map;

import com.blackstrawai.common.BaseRequest;

public class MinimalOrganizationRequest extends BaseRequest{
	
	private int id;
	private String status;
	private String pan;
	private int constitutionId;
	private int gstRegnTypeId;
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
	public String getPan() {
		return pan;
	}
	public void setPan(String pan) {
		this.pan = pan;
	}
	public int getConstitutionId() {
		return constitutionId;
	}
	public void setConstitutionId(int constitutionId) {
		this.constitutionId = constitutionId;
	}
	public int getGstRegnTypeId() {
		return gstRegnTypeId;
	}
	public void setGstRegnTypeId(int gstRegnTypeId) {
		this.gstRegnTypeId = gstRegnTypeId;
	}
	
	public Map<String, String> getGst() {
		return gst;
	}
	public void setGst(Map<String, String> gst) {
		this.gst = gst;
	}
	public String getLegalName() {
		return legalName;
	}
	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public int getOrganizationTypeId() {
		return organizationTypeId;
	}
	public void setOrganizationTypeId(int organizationTypeId) {
		this.organizationTypeId = organizationTypeId;
	}
	public int getIndustryOfOrganization() {
		return industryOfOrganization;
	}
	public void setIndustryOfOrganization(int industryOfOrganization) {
		this.industryOfOrganization = industryOfOrganization;
	}
	public String getUserProfile() {
		return userProfile;
	}
	public void setUserProfile(String userProfile) {
		this.userProfile = userProfile;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	private Map<String,String>gst;
	private List<String>otherGsts;
	public List<String> getOtherGsts() {
		return otherGsts;
	}
	public void setOtherGsts(List<String> otherGsts) {
		this.otherGsts = otherGsts;
	}
	private String legalName;
	private String displayName;
	private int organizationTypeId;
	private int industryOfOrganization;
	private String userProfile;
	private String brandName;
}
