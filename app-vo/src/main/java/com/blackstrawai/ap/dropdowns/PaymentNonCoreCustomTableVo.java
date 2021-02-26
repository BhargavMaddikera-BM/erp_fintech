package com.blackstrawai.ap.dropdowns;

import java.util.List;

public class PaymentNonCoreCustomTableVo {
	List<PaymentNonCoreCustomFieldVo> billPayments;
	List<PaymentNonCoreCustomFieldVo> tdsPayments;
	List<PaymentNonCoreCustomFieldVo> gstPayments;
	List<PaymentNonCoreCustomFieldVo> employeePayments;
	public List<PaymentNonCoreCustomFieldVo> getBillPayments() {
		return billPayments;
	}
	public void setBillPayments(List<PaymentNonCoreCustomFieldVo> billPayments) {
		this.billPayments = billPayments;
	}
	public List<PaymentNonCoreCustomFieldVo> getTdsPayments() {
		return tdsPayments;
	}
	public void setTdsPayments(List<PaymentNonCoreCustomFieldVo> tdsPayments) {
		this.tdsPayments = tdsPayments;
	}
	public List<PaymentNonCoreCustomFieldVo> getGstPayments() {
		return gstPayments;
	}
	public void setGstPayments(List<PaymentNonCoreCustomFieldVo> gstPayments) {
		this.gstPayments = gstPayments;
	}
	public List<PaymentNonCoreCustomFieldVo> getEmployeePayments() {
		return employeePayments;
	}
	public void setEmployeePayments(List<PaymentNonCoreCustomFieldVo> employeePayments) {
		this.employeePayments = employeePayments;
	}
	
	
	
}
