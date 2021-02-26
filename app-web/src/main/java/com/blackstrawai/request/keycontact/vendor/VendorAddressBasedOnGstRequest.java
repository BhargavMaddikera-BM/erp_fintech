package com.blackstrawai.request.keycontact.vendor;

public class VendorAddressBasedOnGstRequest extends VendorBasedOnGstRequest{

	
	private Integer vendorId;
	
	private Integer orgId;
	
	private String roleName;
	
	private String userId;
	
	
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

		
}
