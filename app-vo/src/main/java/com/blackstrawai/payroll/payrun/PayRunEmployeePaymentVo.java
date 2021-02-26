package com.blackstrawai.payroll.payrun;

public class PayRunEmployeePaymentVo{
	
	private String empId;
	private String payrunReference;
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getPayrunReference() {
		return payrunReference;
	}
	public void setPayrunReference(String payrunReference) {
		this.payrunReference = payrunReference;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	private double amount;

}
