package com.blackstrawai.ap.payment.noncore;

public class PaymentNonCoreEmployeeVo {
	private int id;
	private String status;
	private String empName;
	private String payable;
	private String advance;
	private String netPay;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getPayable() {
		return payable;
	}
	public void setPayable(String payable) {
		this.payable = payable;
	}
	public String getAdvance() {
		return advance;
	}
	public void setAdvance(String advance) {
		this.advance = advance;
	}
	public String getNetPay() {
		return netPay;
	}
	public void setNetPay(String netPay) {
		this.netPay = netPay;
	}
	
}
