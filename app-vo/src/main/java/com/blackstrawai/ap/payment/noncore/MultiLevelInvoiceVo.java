package com.blackstrawai.ap.payment.noncore;

import java.util.List;

public class MultiLevelInvoiceVo {
	private int id;
	private String name;
	private List<PaymentInvoiceDetailsVo> child;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<PaymentInvoiceDetailsVo> getChild() {
		return child;
	}
	public void setChild(List<PaymentInvoiceDetailsVo> child) {
		this.child = child;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
