package com.blackstrawai.request.payroll.PayRun;

import java.math.BigDecimal;
import java.util.List;

import com.blackstrawai.common.BaseRequest;
import com.blackstrawai.journals.GeneralLedgerVo;
import com.blackstrawai.payroll.payrun.PayRunTableVo;
import com.blackstrawai.request.attachments.UploadFileRequest;
import com.fasterxml.jackson.core.JsonProcessingException;

public class PayRunRequest extends BaseRequest {
	
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
	
	private BigDecimal payRunAmount;
	
	private BigDecimal payRunPaidAmount;
	
//	private List<PayRunInformationRequest> payRunInformation;
	private Object payRunInformation;
	
	private List<UploadFileRequest> attachments;
	
	private List<Integer> attachmentsToRemove ;
	
	private String payRunRefPrefix;
	
	private String payRunRefSuffix;
	
	private List<PayRunTableVo> settingsData;
	
	private Integer currencyId;
	
	private GeneralLedgerVo generalLedgerData;

	

	public GeneralLedgerVo getGeneralLedgerData() {
		return generalLedgerData;
	}

	public void setGeneralLedgerData(GeneralLedgerVo generalLedgerData) {
		this.generalLedgerData = generalLedgerData;
	}

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

	public String getPaymentCycle() {
		return paymentCycle;
	}

	public void setPaymentCycle(String paymentCycle) {
		this.paymentCycle = paymentCycle;
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

	public Object getPayRunInformation() {
		return payRunInformation;
	}

	public void setPayRunInformation(Object payRunInformation) throws JsonProcessingException {
//		ObjectMapper mapper = new ObjectMapper();
//		String jsn = mapper.writeValueAsString(payRunInformation);
		this.payRunInformation = payRunInformation;
	}

	public List<UploadFileRequest> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<UploadFileRequest> payRunAttachment) {
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
		return "PayRunRequest [payRunId=" + payRunId + ", orgId=" + orgId + ", UserId=" + UserId + ", isSuperAdmin="
				+ isSuperAdmin + ", roleName=" + roleName + ", payrunReference=" + payrunReference + ", payrunDate="
				+ payrunDate + ", payPeriod=" + payPeriod + ", paymentCycle=" + paymentCycle + ", status=" + status
				+ ", copyPreviousPayRun=" + copyPreviousPayRun + ", employeeCount=" + employeeCount + ", payRunAmount="
				+ payRunAmount + ", payRunPaidAmount=" + payRunPaidAmount + ", payRunInformation=" + payRunInformation
				+ ", payRunAttachment=" + attachments + ", attachmentsToRemove=" + attachmentsToRemove
				+ ", payRunRefPrefix=" + payRunRefPrefix + ", payRunRefSuffix=" + payRunRefSuffix + ", settingsData="
				+ settingsData + ", currencyId=" + currencyId + "]";
	}

    
	

	
	



	

	


	

}
