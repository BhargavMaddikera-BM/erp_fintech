package com.blackstrawai.ap.purchaseorder;

import com.blackstrawai.common.BaseVo;

public class PoFilterVo extends BaseVo{

	private Integer orgId;
	
	private String status;
	
	private Integer vendorId;
	
	private Integer fromAmount;
	
	private Integer toAmount;
	
	private String startDate;
	
	private String endDate;
	
	private String roleName;
	
	

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public Integer getFromAmount() {
		return fromAmount;
	}

	public void setFromAmount(Integer fromAmount) {
		this.fromAmount = fromAmount;
	}

	public Integer getToAmount() {
		return toAmount;
	}

	public void setToAmount(Integer toAmount) {
		this.toAmount = toAmount;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PoFilterRequest [orgId=");
		builder.append(orgId);
		builder.append(", status=");
		builder.append(status);
		builder.append(", vendorId=");
		builder.append(vendorId);
		builder.append(", fromAmount=");
		builder.append(fromAmount);
		builder.append(", toAmount=");
		builder.append(toAmount);
		builder.append(", startDate=");
		builder.append(startDate);
		builder.append(", endDate=");
		builder.append(endDate);
		builder.append("]");
		return builder.toString();
	}
	
	
}
