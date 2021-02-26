package com.blackstrawai.report;

import java.util.List;


import com.blackstrawai.common.BaseVo;

public class TrialBalanceReportVo extends BaseVo {	

	
	private Integer orgId;
	
	private String startDate;
	
	private String endDate;
	
	private String roleName;
	
	private Boolean isSuperAdmin;
	
	private List<TrialBalanceReportGeneralVo> trialBalanceData;	

	private String status;

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
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

	public List<TrialBalanceReportGeneralVo> getTrialBalanceData() {
		return trialBalanceData;
	}

	public void setTrialBalanceData(List<TrialBalanceReportGeneralVo> trialBalanceData) {
		this.trialBalanceData = trialBalanceData;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
		return "TrialBalanceVo [orgId=" + orgId + ", startDate=" + startDate + ", endDate=" + endDate + ", roleName="
				+ roleName + ", isSuperAdmin=" + isSuperAdmin + ", trialBalanceData=" + trialBalanceData + ", status="
				+ status + "]";
	}

	
	

}
