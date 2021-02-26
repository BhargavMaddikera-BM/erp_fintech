package com.blackstrawai.ap.dropdowns;

import java.util.List;

public class BasicContactVo {
	private List<PaymentTypeVo> statutoryBody;
	private List<PaymentTypeVo> vendors;
	private List<PaymentTypeVo> employees;
	private List<PaymentTypeVo> customers;
	public List<PaymentTypeVo> getStatutoryBody() {
		return statutoryBody;
	}
	public void setStatutoryBody(List<PaymentTypeVo> statutoryBody) {
		this.statutoryBody = statutoryBody;
	}
	public List<PaymentTypeVo> getVendors() {
		return vendors;
	}
	public void setVendors(List<PaymentTypeVo> vendors) {
		this.vendors = vendors;
	}
	public List<PaymentTypeVo> getEmployees() {
		return employees;
	}
	public void setEmployees(List<PaymentTypeVo> employees) {
		this.employees = employees;
	}
	public List<PaymentTypeVo> getCustomers() {
		return customers;
	}
	public void setCustomers(List<PaymentTypeVo> customers) {
		this.customers = customers;
	}
	
	
	
}
