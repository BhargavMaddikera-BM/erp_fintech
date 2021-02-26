package com.blackstrawai.request.gst;

import com.blackstrawai.common.BaseRequest;

public class PanGstRequest extends BaseRequest {

	private String gstNumber;
	private String roleName;
	private Integer organizationId;

	public String getGstNumber() {
		return gstNumber;
	}

	public String getRoleName() {
		return roleName;
	}

	public Integer getOrganizationId() {
		return organizationId;
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

	@Override
	public String toString() {
		return "PanGstRequest [gstNumber=" + gstNumber + ", roleName=" + roleName + ", organizationId=" + organizationId
				+ "]";
	}

}
