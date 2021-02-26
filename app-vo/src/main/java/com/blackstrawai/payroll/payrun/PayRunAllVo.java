package com.blackstrawai.payroll.payrun;

import java.math.BigDecimal;
import java.util.List;

import com.blackstrawai.attachments.UploadFileVo;

public class PayRunAllVo {
	
    private Integer payRunId;
	
	private Integer orgId;
	
	private String UserId;	
	
	private Boolean isSuperAdmin;
	
	private String roleName;		
		
	private String payrunReference;
	
	private String payrunDate ;
	
	private String payPeriod;

	private String status;
	
	public String copyPreviousPayRun;
	
	private Integer employeeCount;
	
	private BigDecimal payRunAmount;
	
	private BigDecimal payRunPaidAmount;
	
	private List<PayRunInformationVo> payRunInformation;
	
	private List<UploadFileVo> payRunAttachment;

	public Integer getPayRunId() {
		return payRunId;
	}

	public void setPayRunId(Integer payRunId) {
		this.payRunId = payRunId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
	}

	public Boolean getIsSuperAdmin() {
		return isSuperAdmin;
	}

	public void setIsSuperAdmin(Boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getPayrunReference() {
		return payrunReference;
	}

	public void setPayrunReference(String payrunReference) {
		this.payrunReference = payrunReference;
	}

	public String getPayrunDate() {
		return payrunDate;
	}

	public void setPayrunDate(String payrunDate) {
		this.payrunDate = payrunDate;
	}

	public String getPayPeriod() {
		return payPeriod;
	}

	public void setPayPeriod(String payPeriod) {
		this.payPeriod = payPeriod;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCopyPreviousPayRun() {
		return copyPreviousPayRun;
	}

	public void setCopyPreviousPayRun(String copyPreviousPayRun) {
		this.copyPreviousPayRun = copyPreviousPayRun;
	}

	public Integer getEmployeeCount() {
		return employeeCount;
	}

	public void setEmployeeCount(Integer employeeCount) {
		this.employeeCount = employeeCount;
	}

	public BigDecimal getPayRunAmount() {
		return payRunAmount;
	}

	public void setPayRunAmount(BigDecimal payRunAmount) {
		this.payRunAmount = payRunAmount;
	}

	public BigDecimal getPayRunPaidAmount() {
		return payRunPaidAmount;
	}

	public void setPayRunPaidAmount(BigDecimal payRunPaidAmount) {
		this.payRunPaidAmount = payRunPaidAmount;
	}

	public List<PayRunInformationVo> getPayRunInformation() {
		return payRunInformation;
	}

	public void setPayRunInformation(List<PayRunInformationVo> payRunInformation) {
		this.payRunInformation = payRunInformation;
	}

	public List<UploadFileVo> getPayRunAttachment() {
		return payRunAttachment;
	}

	public void setPayRunAttachment(List<UploadFileVo> payRunAttachment) {
		this.payRunAttachment = payRunAttachment;
	}

	

}
