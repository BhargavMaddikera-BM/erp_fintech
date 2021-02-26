package com.blackstrawai.gst;

import com.blackstrawai.common.TokenVo;

public class PanGstVo extends TokenVo {

	private String gstNumber;
	private String roleName;
	private Integer organizationId;
	private String userId;
	private String panNo;

	public String getGstNumber() {
		return gstNumber;
	}

	public String getRoleName() {
		return roleName;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public String getUserId() {
		return userId;
	}

	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	@Override
	public String toString() {
		return "PanGstVo [gstNumber=" + gstNumber + ", roleName=" + roleName + ", organizationId=" + organizationId
				+ ", userId=" + userId + ", panNo=" + panNo + "]";
	}

}
