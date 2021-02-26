package com.blackstrawai.request.upload;

import java.util.List;

import com.blackstrawai.common.BaseRequest;
import com.blackstrawai.request.payroll.PayrollUploadRequest;

public class CreateImportFileRequest extends BaseRequest {

	private String moduleName;
	private Integer orgId;
	private String userId;
	private boolean isSuperAdmin;
	private boolean duplicacy;
	private String roleName = "SuperAdmin";
	private String status;
	
	private List<VendorUploadRequest> vendor;
	private List<CustomerUploadRequest> customer;
	private List<EmployeeUploadRequest> employee;
	private List<AccountingEntriesUploadRequest> accountingEntries;
	private List<PayrollUploadRequest> payRunInformation;
	private List<ContraUploadFileRequest> contra;
	private List<BulkPaymentUploadRequest> bulkPayment;
	

	public List<BulkPaymentUploadRequest> getBulkPayment() {
		return bulkPayment;
	}

	public void setBulkPayment(List<BulkPaymentUploadRequest> bulkPayment) {
		this.bulkPayment = bulkPayment;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}



	public List<ContraUploadFileRequest> getContra() {
		return contra;
	}

	public void setContra(List<ContraUploadFileRequest> contra) {
		this.contra = contra;
	}

	public List<PayrollUploadRequest> getPayRunInformation() {
		return payRunInformation;
	}

	public void setPayRunInformation(List<PayrollUploadRequest> payroll) {
		this.payRunInformation = payroll;
	}

	public List<AccountingEntriesUploadRequest> getAccountingEntries() {
		return accountingEntries;
	}

	public void setAccountingEntries(List<AccountingEntriesUploadRequest> accountingEntries) {
		this.accountingEntries = accountingEntries;
	}

	public boolean isDuplicacy() {
		return duplicacy;
	}

	public void setDuplicacy(boolean duplicacy) {
		this.duplicacy = duplicacy;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean isSuperAdmin() {
		return isSuperAdmin;
	}

	public void setSuperAdmin(boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public List<VendorUploadRequest> getVendor() {
		return vendor;
	}

	public void setVendor(List<VendorUploadRequest> vendor) {
		this.vendor = vendor;
	}

	public List<CustomerUploadRequest> getCustomer() {
		return customer;
	}

	public void setCustomer(List<CustomerUploadRequest> customer) {
		this.customer = customer;
	}

	public List<EmployeeUploadRequest> getEmployee() {
		return employee;
	}

	public void setEmployee(List<EmployeeUploadRequest> employee) {
		this.employee = employee;
	}

	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	@Override
	public String toString() {
		return "CreateImportFileRequest [moduleName=" + moduleName + ", orgId=" + orgId + ", userId=" + userId
				+ ", isSuperAdmin=" + isSuperAdmin + ", duplicacy=" + duplicacy + ", roleName=" + roleName + ", vendor="
				+ vendor + ", customer=" + customer + ", employee=" + employee + ", accountingEntries="
				+ accountingEntries + ", payroll=" + payRunInformation + ", contra=" + contra + "]";
	}

	

	
}
