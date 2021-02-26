package com.blackstrawai.response.upload;

import java.util.List;

import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.upload.AccountingEntriesUploadVo;
import com.blackstrawai.upload.ContraUploadVo;
import com.blackstrawai.upload.CustomerUploadVo;
import com.blackstrawai.upload.EmployeeUploadVo;
import com.blackstrawai.upload.PayrollUploadVo;
import com.blackstrawai.upload.VendorUploadVo;

public class ProcessImportFileResponse extends BaseResponse {

	private List<VendorUploadVo> vendor;
	private List<CustomerUploadVo> customer;
	private List<EmployeeUploadVo> employee;
	private List<AccountingEntriesUploadVo> accountingEntries;
	private List<PayrollUploadVo> payRunInformation;
	private List<ContraUploadVo> contra;
	

	public List<ContraUploadVo> getContra() {
		return contra;
	}

	public void setContra(List<ContraUploadVo> contra) {
		this.contra = contra;
	}

	public List<PayrollUploadVo> getPayRunInformation() {
		return payRunInformation;
	}

	public void setPayRunInformation(List<PayrollUploadVo> payroll) {
		this.payRunInformation = payroll;
	}

	public List<AccountingEntriesUploadVo> getAccountingEntries() {
		return accountingEntries;
	}

	public void setAccountingEntries(List<AccountingEntriesUploadVo> accountingEntries) {
		this.accountingEntries = accountingEntries;
	}

	public List<VendorUploadVo> getVendor() {
		return vendor;
	}

	public void setVendor(List<VendorUploadVo> vendor) {
		this.vendor = vendor;
	}

	public List<CustomerUploadVo> getCustomer() {
		return customer;
	}

	public void setCustomer(List<CustomerUploadVo> customer) {
		this.customer = customer;
	}

	public List<EmployeeUploadVo> getEmployee() {
		return employee;
	}

	public void setEmployee(List<EmployeeUploadVo> employee) {
		this.employee = employee;
	}

}
