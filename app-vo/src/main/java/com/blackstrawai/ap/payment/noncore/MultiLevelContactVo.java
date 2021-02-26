package com.blackstrawai.ap.payment.noncore;

import java.util.List;

import com.blackstrawai.ap.dropdowns.PaymentTypeVo;

public class MultiLevelContactVo {
	private int id;
	private String name;
	private List<PaymentTypeVo> child;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<PaymentTypeVo> getChild() {
		return child;
	}
	public void setChild(List<PaymentTypeVo> child) {
		this.child = child;
	}
	
	
}
