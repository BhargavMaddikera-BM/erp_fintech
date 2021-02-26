package com.blackstrawai.payroll.payrun;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.List;

import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseVo;
import com.blackstrawai.journals.GeneralLedgerVo;

public class PayRunVo extends BaseVo {
	
    private Integer payRunId;
	
	private Integer orgId;
	
	private String UserId;	
	
	private Boolean isSuperAdmin;
	
	private String roleName;		
		
	private String payrunReference;
	
	private String payrunDate ;
	
	private String payPeriod;
	
	private String paymentCycle;

	private String status;
	
	public String copyPreviousPayRun;
	
	private Integer employeeCount;
	
	private String payRunAmount;
	
	private BigDecimal payRunPaidAmount;
	
	private Date createTs;
	

	
//	private List<PayRunInformationVo> payRunInformation;
	
	public Date getCreateTs() {
		return createTs;
	}

	public void setCreateTs(Date createTs) {
		this.createTs = createTs;
	}

	private Object payRunInformation;
	
	private List<UploadFileVo> attachments;
	
	private List<Integer> attachmentsToRemove ;
	
	private String payRunRefPrefix;
	
	private String payRunRefSuffix;
	
	private GeneralLedgerVo generalLedgerData;
	
	private List<PayRunTableVo> settingsData;
	
	private Integer currencyId;
	

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

	public Object getPayRunInformation() {
		return payRunInformation;
	}

	public void setPayRunInformation(Object payRunInformation) {
		this.payRunInformation = payRunInformation;
	}

	public String getPayrunReference() {
		return payrunReference;
	}

	public void setPayrunReference(String payrun_reference) {
		this.payrunReference = payrun_reference;
	}

	public String getPayrunDate() {
		return payrunDate;
	}

	public void setPayrunDate(String payrun_date) {
		this.payrunDate = payrun_date;
	}

	public String getPayPeriod() {
		return payPeriod;
	}

	public void setPayPeriod(String pay_period) {
		this.payPeriod = pay_period;
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
	
	public String getPaymentCycle() {
		return paymentCycle;
	}

	public void setPaymentCycle(String paymentCycle) {
		this.paymentCycle = paymentCycle;
	}

	

	public Integer getEmployeeCount() {
		return employeeCount;
	}

	public void setEmployeeCount(Integer employeeCount) {
		this.employeeCount = employeeCount;
	}

	public String getPayRunAmount() {
		return payRunAmount; 
		
	}

	public void setPayRunAmount(String payRunAmount2) {
		this.payRunAmount = payRunAmount2;
	}

	public BigDecimal getPayRunPaidAmount() {
		return payRunPaidAmount;

		
	}

	public void setPayRunPaidAmount(BigDecimal payRunPaidAmount) {
		this.payRunPaidAmount = payRunPaidAmount;
	}

	
	
	public List<UploadFileVo> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<UploadFileVo> payRunAttachment) {
		this.attachments = payRunAttachment;
	}
	
	

	public List<Integer> getAttachmentsToRemove() {
		return attachmentsToRemove;
	}

	public void setAttachmentsToRemove(List<Integer> attachmentsToRemove) {
		this.attachmentsToRemove = attachmentsToRemove;
	}

	public String getPayRunRefPrefix() {
		return payRunRefPrefix;
	}

	public void setPayRunRefPrefix(String payRunRefPrefix) {
		this.payRunRefPrefix = payRunRefPrefix;
	}

	public String getPayRunRefSuffix() {
		return payRunRefSuffix;
	}

	public void setPayRunRefSuffix(String payRunRefSuffix) {
		this.payRunRefSuffix = payRunRefSuffix;
	}

	public GeneralLedgerVo getGeneralLedgerData() {
		return generalLedgerData;
	}
	public void setGeneralLedgerData(GeneralLedgerVo generalLedgerData) {
		this.generalLedgerData = generalLedgerData;
	}

	

	public List<PayRunTableVo> getSettingsData() {
		return settingsData;
	}

	public void setSettingsData(List<PayRunTableVo> settingsData) {
		this.settingsData = settingsData;
	}
	
	

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	@Override
	public String toString() {
		return "PayRunVo [payRunId=" + payRunId + ", orgId=" + orgId + ", UserId=" + UserId + ", isSuperAdmin="
				+ isSuperAdmin + ", roleName=" + roleName + ", payrunReference=" + payrunReference + ", payrunDate="
				+ payrunDate + ", payPeriod=" + payPeriod + ", paymentCycle=" + paymentCycle + ", status=" + status
				+ ", copyPreviousPayRun=" + copyPreviousPayRun + ", employeeCount=" + employeeCount + ", payRunAmount="
				+ payRunAmount + ", payRunPaidAmount=" + payRunPaidAmount + ", payRunInformation=" + payRunInformation
				+ ", payRunAttachment=" + attachments + ", attachmentsToRemove=" + attachmentsToRemove
				+ ", payRunRefPrefix=" + payRunRefPrefix + ", payRunRefSuffix=" + payRunRefSuffix
				+ ", generalLedgerData=" + generalLedgerData + ", settingsData=" + settingsData + ", currencyId="
				+ currencyId + "]";
	}

	


	

	
	
	

	
	
	


}
