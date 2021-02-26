package com.blackstrawai.report;

import java.util.List;

import com.blackstrawai.common.BaseVo;

public class BalanceSheetReportVo extends BaseVo {
	
private Integer orgId;
	
	private String startDate;
	
	private String endDate;
	
	private String roleName;
	
	private Boolean isSuperAdmin;
	
	private List<BalanceSheetReportGeneralVo> balanceSheetData;	

	private String status;

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
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

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}

	public void setIsSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public List<BalanceSheetReportGeneralVo> getBalanceSheetData() {
		return balanceSheetData;
	}

	public void setBalanceSheetData(List<BalanceSheetReportGeneralVo> balanceSheetData) {
		this.balanceSheetData = balanceSheetData;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "BalanceSheetVo [orgId=" + orgId + ", startDate=" + startDate + ", endDate=" + endDate + ", roleName="
				+ roleName + ", isSuperAdmin=" + isSuperAdmin + ", balanceSheetData=" + balanceSheetData + ", status="
				+ status + "]";
	}
	
	

}
