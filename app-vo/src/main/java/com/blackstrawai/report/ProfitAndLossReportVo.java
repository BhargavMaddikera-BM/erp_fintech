package com.blackstrawai.report;

import java.util.List;

import com.blackstrawai.common.BaseVo;

public class ProfitAndLossReportVo extends BaseVo {
	
    private Integer orgId;
	
	private String startDate;
	
	private String endDate;
	
	private String roleName;
	
	private Boolean isSuperAdmin;
	
	private List<ProfitAndLossReportGeneralVo> profitAndLossData;	

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

	public List<ProfitAndLossReportGeneralVo> getProfitAndLossData() {
		return profitAndLossData;
	}

	public void setProfitAndLossData(List<ProfitAndLossReportGeneralVo> profitAndLossData) {
		this.profitAndLossData = profitAndLossData;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
